package stablo;

import java.awt.*;
import java.awt.event.*;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import javax.swing.*;
import javax.swing.event.*;

public class GUI extends JComponent {

    private static final int WIDE = 640;
    private static final int HIGH = 480;
    private static final int RADIUS = 75;
    private ControlPanel control = new ControlPanel();
    private int radius = RADIUS;
    private List<Node> nodes = new ArrayList<Node>();
    private List<Node> selected = new ArrayList<Node>();
    private List<Edge> edges = new ArrayList<Edge>();
    private List<Osoba> osobe = new ArrayList<>();
    private Point mousePt = new Point(WIDE / 2, HIGH / 2);
    private Rectangle mouseRect = new Rectangle();
    private boolean selecting = false;

    public static void main(String[] args) throws Exception {
        EventQueue.invokeLater(new Runnable() {

            public void run() {
                JFrame f = new JFrame("Porodicno stablo");
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                GUI gp = new GUI();
                f.add(gp.control, BorderLayout.NORTH);
                f.add(new JScrollPane(gp), BorderLayout.CENTER);
                f.getRootPane().setDefaultButton(gp.control.defaultButton);
                f.pack();
                f.setLocationByPlatform(true);
                f.setVisible(true);
            }
        });
    }

    public GUI() {
        this.setOpaque(true);
        this.addMouseListener(new MouseHandler());
        this.addMouseMotionListener(new MouseMotionHandler());
    }

    @Override
    public Dimension getPreferredSize() {
        return new Dimension(WIDE, HIGH);
    }

    //ova metoda se poziva kada je napisano "repaint()",
    @Override
    public void paintComponent(Graphics g) {
        g.setColor(new Color(0x00f0f0f0));
        g.fillRect(0, 0, getWidth(), getHeight());
        // poziva se metoda draw() iz Egde
        for (Edge e : edges) {
            e.draw(g);
        }
        // poziva se metoda draw() iz Node i crta se node
        for (Node n : nodes) {
            n.draw(g);
        }
        if (selecting) {
            g.setColor(Color.darkGray);
            g.drawRect(mouseRect.x, mouseRect.y, mouseRect.width, mouseRect.height);
        }
    }

    private class MouseHandler extends MouseAdapter {

        @Override
        public void mouseReleased(MouseEvent e) {
            selecting = false;
            mouseRect.setBounds(0, 0, 0, 0);
            if (e.isPopupTrigger()) {
                showPopup(e);
            }
            e.getComponent().repaint();
        }

        @Override
        public void mousePressed(MouseEvent e) {
            mousePt = e.getPoint();
            if (e.isShiftDown()) {
                Node.selectToggle(nodes, mousePt);
            } else if (e.isPopupTrigger()) {
                Node.selectOne(nodes, mousePt);
                showPopup(e);
            } else if (Node.selectOne(nodes, mousePt)) {
                selecting = false;
            } else {
                Node.selectNone(nodes);
                selecting = true;
            }
            e.getComponent().repaint();
        }

        private void showPopup(MouseEvent e) {
            control.popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    private class MouseMotionHandler extends MouseMotionAdapter {

        Point delta = new Point();

        @Override
        public void mouseDragged(MouseEvent e) {
            if (selecting) {
                mouseRect.setBounds(
                        Math.min(mousePt.x, e.getX()),
                        Math.min(mousePt.y, e.getY()),
                        Math.abs(mousePt.x - e.getX()),
                        Math.abs(mousePt.y - e.getY()));
                Node.selectRect(nodes, mouseRect);
            } else {
                delta.setLocation(
                        e.getX() - mousePt.x,
                        e.getY() - mousePt.y);
                Node.updatePosition(nodes, delta);
                mousePt = e.getPoint();
            }
            e.getComponent().repaint();
        }
    }

    public JToolBar getControlPanel() {
        return control;
    }

    // popup meni i meni na vrhu
    private class ControlPanel extends JToolBar {

        // kreiraju se dugmici za menije
        private Action newNode = new NewNodeAction("Nova osoba");
        private Action clearAll = new ClearAction("Obrisi sve");
        private Action name = new NameAction("Promeni informacije");
        private Action info = new InfoAction("Prikazi informacije");
        private Action color = new ColorAction("Boja pravougaonika");
        private Action connect = new ConnectAction("Povezi");
        private Action delete = new DeleteAction("Obrisi osobu");
        private JButton defaultButton = new JButton(newNode);
        private JComboBox kindCombo = new JComboBox();
        private ColorIcon hueIcon = new ColorIcon(Color.green);
        private JPopupMenu popup = new JPopupMenu();

        ControlPanel() {
            this.setLayout(new FlowLayout(FlowLayout.LEFT));
            this.setBackground(Color.lightGray);

            //ove dve stvari se dodaju na glavni meni gore
            this.add(defaultButton);
            this.add(new JButton(clearAll));

            JSpinner js = new JSpinner();
            js.setModel(new SpinnerNumberModel(RADIUS, 10, 100, 5));
            js.addChangeListener(new ChangeListener() {

                @Override
                public void stateChanged(ChangeEvent e) {
                    JSpinner s = (JSpinner) e.getSource();
                    radius = (Integer) s.getValue();
                    Node.updateRadius(nodes, radius);
                    GUI.this.repaint();
                }
            });

            // ubacivanje elemenata u meni koji se otvara na desni klik
            popup.add(new JMenuItem(newNode));
            popup.add(new JMenuItem(name));
            popup.add(new JMenuItem(info));
            popup.add(new JMenuItem(connect));
            popup.add(new JMenuItem(delete));
            popup.add(new JMenuItem(color));
            popup.add(js);
        }
    }

    // kad se klikne dugme Obrisi sve, ovo se pokrene
    private class ClearAction extends AbstractAction {

        public ClearAction(String name) {
            super(name);
        }

        public void actionPerformed(ActionEvent e) {
            nodes.clear();
            edges.clear();
            osobe.clear();
            repaint();
        }
    }

    // za promene boja
    private class ColorAction extends AbstractAction {

        public ColorAction(String name) {
            super(name);
        }

        public void actionPerformed(ActionEvent e) {
            Color color = control.hueIcon.getColor();
            color = JColorChooser.showDialog(
            		GUI.this, "Choose a color", color);
            if (color != null) {
                Node.updateColor(nodes, color);
                control.hueIcon.setColor(color);
                control.repaint();
                repaint();
            }
        }
    }

    // kada se klikne Povezi, povezuje sve selektovane elemente
    private class ConnectAction extends AbstractAction {

        public ConnectAction(String name) {
            super(name);
        }

        public void actionPerformed(ActionEvent e) {
            Node.getSelected(nodes, selected);
            if (selected.size() > 1) {
                for (int i = 0; i < selected.size() - 1; ++i) {
                    Node n1 = selected.get(i);
                    Node n2 = selected.get(i + 1);
                    edges.add(new Edge(n1, n2));
                }
            }
            repaint();
        }
    }

    // za brisanje selektovanih elemenata
    private class DeleteAction extends AbstractAction {

        public DeleteAction(String name) {
            super(name);
        }

        public void actionPerformed(ActionEvent e) {
            ListIterator<Node> iter = nodes.listIterator();
            while (iter.hasNext()) {
                Node n = iter.next();
                if (n.isSelected()) {
                    deleteEdges(n);
                    iter.remove();
                }
            }
            repaint();
        }

        private void deleteEdges(Node n) {
            ListIterator<Edge> iter = edges.listIterator();
            while (iter.hasNext()) {
                Edge e = iter.next();
                if (e.n1 == n || e.n2 == n) {
                    iter.remove();
                }
            }
        }
    }

    // ovo se pokrece kada se klikne na Promeni informacije u popup meniju
    private class NameAction extends AbstractAction {

        public NameAction(String name) {
            super(name);
        }

        public void actionPerformed(ActionEvent e) {
            String ime = "";
            String prezime = "";
            String tekst = "Unesite sve podatke";
            Pol pol = Pol.Musko;
            int day = 1;
            int month = 1;
            int year = 1990;
            boolean again = true;

            while (again) {
                // inicijacizacija elementa za prozor gde se unose podaci o osobi
                JLabel txt = new JLabel("");
                txt.setText(tekst);
                JTextField fieldIme = new JTextField(12);
                fieldIme.setText(ime);
                JTextField fieldPrezime = new JTextField(12);
                fieldPrezime.setText(prezime);

                JComboBox polCombo = new JComboBox();
                for (Pol p : Pol.values()){
                    polCombo.addItem(p);
                }
                polCombo.setSelectedItem(pol);

                JComboBox dayCombo = new JComboBox();
                for (int i = 1; i < 32; i++){
                    dayCombo.addItem(i);
                }
                dayCombo.setSelectedItem(day);

                JComboBox monthCombo = new JComboBox();
                for (int i = 1; i < 13; i++){
                    monthCombo.addItem(i);
                }
                monthCombo.setSelectedItem(month);

                JComboBox yearCombo = new JComboBox();
                for (int i = 1920; i < 2022; i++){
                    yearCombo.addItem(i);
                }
                yearCombo.setSelectedItem(year);

                // ubacivanje elemenata u panel
                JPanel myPanel = new JPanel();
                myPanel.add(txt);
                myPanel.add(Box.createVerticalStrut(1));
                myPanel.add(new JLabel("Ime:"));
                myPanel.add(fieldIme);
                myPanel.add(new JLabel("Prezime:"));
                myPanel.add(fieldPrezime);

                myPanel.add(new JLabel("Pol:"));
                myPanel.add(polCombo);
                myPanel.add(new JLabel("Dan rodjenja:"));
                myPanel.add(dayCombo);

                myPanel.add(new JLabel("Mesec rodjenja:"));
                myPanel.add(monthCombo);
                myPanel.add(new JLabel("Godina rodjenja:"));
                myPanel.add(yearCombo);

                myPanel.setLayout(new GridLayout(0, 1));

                int result = JOptionPane.showConfirmDialog(null, myPanel, "Osoba", JOptionPane.OK_CANCEL_OPTION);

                    // ako se kline ok, proverava se da li je uneto ime ili prezime, ako nije uneseno pokrece ponovo panel sve dok  se ne unesu ili se klikne CANCEL
                if (result == JOptionPane.OK_OPTION) {
                    if (!fieldIme.getText().equals("") && !fieldPrezime.getText().equals("")) {

                        // preuzimanje vrednosti  iz panela
                        ime = fieldIme.getText();
                        prezime = fieldPrezime.getText();
                        pol = (Pol) polCombo.getSelectedItem();
                        day = (int) dayCombo.getSelectedItem();
                        month = (int) monthCombo.getSelectedItem();
                        year = (int) yearCombo.getSelectedItem();
                        String name = fieldIme.getText() + " " + fieldPrezime.getText();

                        // kreiranje osobe
                        Datum datum = new Datum(day, month, year);
                        Osoba osoba = new Osoba(ime, prezime, pol.toString(), datum);
                        int id = osoba.getId();
                        osobe.add(osoba);

                        // promena imena jednog cvora
                        Node.updateName(nodes, name, id);
                        repaint();
                        again = false;
                    } else {
                        // cuvanje unesenih vrednosti
                        ime = fieldIme.getText();
                        prezime = fieldPrezime.getText();
                        pol = (Pol) polCombo.getSelectedItem();
                        day = (int) dayCombo.getSelectedItem();
                        month = (int) monthCombo.getSelectedItem();
                        year = (int) yearCombo.getSelectedItem();
                        tekst = "Morate uneti ime i prezime osobe!";
                    }
                } else if (result  == JOptionPane.CANCEL_OPTION) {
                    again = false;
                }
            }
        }
    }

    private enum Pol {
        Musko, Zensko;
    }

    // prikaz informacija o selektovanom cvoru
    private class InfoAction extends AbstractAction {

        public InfoAction(String name) {
            super(name);
        }

        public void actionPerformed(ActionEvent e) {

                JPanel myPanel = new JPanel();
                int id = -1;
                // trazi selektovani cvor i uzima njegov id
                for(Node n : nodes){
                    if(n.isSelected()){
                        id = n.getId();
                    }
                }

                // ukoliko cvor ima zadato ime id mu je razlicito od -1
                if(id != -1){
                    id--;
                    // uzimanje podataka o osobi i prikaz
                    Osoba osoba = osobe.get(id);
                    Datum datum = osoba.getDatRodj();
                    myPanel.add(new JLabel(osoba.ime + " " + osoba.prezime));
                    myPanel.add(new JLabel("Pol: " + osoba.pol));
                    myPanel.add(new JLabel("Datum rodjenja: " + datum));
                    myPanel.add(new JLabel("ID: " + osoba.id));

                    myPanel.setLayout(new GridLayout(0, 1));
                } else {
                    myPanel.add(new JLabel("..."));
                }
                JOptionPane.showConfirmDialog(null, myPanel, "Informacije o osobi", JOptionPane.OK_CANCEL_OPTION);
        }
    }

    // kreiranje novog cvora klikom na Nova osoba
    private class NewNodeAction extends AbstractAction {

        public NewNodeAction(String name) {
            super(name);
        }

        public void actionPerformed(ActionEvent e) {
            Node.selectNone(nodes);
            Point p = mousePt.getLocation();
            Color color = control.hueIcon.getColor();
            String name = "...";
            Node n = new Node(p, radius, color, name);
            n.setSelected(true);
            nodes.add(n);
            repaint();
        }
    }

    // kreiranje veza (linije) izmedju cvorova
    private static class Edge {

        private Node n1;
        private Node n2;

        public Edge(Node n1, Node n2) {
            this.n1 = n1;
            this.n2 = n2;
        }

        public void draw(Graphics g) {
            Point p1 = n1.getLocation();
            Point p2 = n2.getLocation();
            g.setColor(Color.darkGray);
            g.drawLine(p1.x, p1.y, p2.x, p2.y);
        }
    }

    // kreiranje cvorova i metode za rukovanje cvorom
    private static class Node {

        private int id = -1;
        private Point p;
        private int r;
        private Color color;
        private String name;
        private boolean selected = false;
        private Rectangle b = new Rectangle();

        public Node(Point p, int r, Color color, String name) {
            this.p = p;
            this.r = r;
            this.color = color;
            this.name = name;
            setBoundary(b);
        }

        private void setBoundary(Rectangle b) {
            b.setBounds(p.x - r, p.y - r, 2 * r, 2 * r);
        }

        public void draw(Graphics g) {

            g.setColor(this.color);
            // boji se pozadina cvora
            g.fillRect(b.x, b.y, b.width, 30);

            if (selected) {
                // boji se ivica ukoliko je selektovan cvor
                g.setColor(Color.red);
                g.drawRect(b.x, b.y, b.width, 30);
            }
            
            Font font = new Font("Arial", Font.BOLD, 14);
            g.setFont(font);
            g.setColor(Color.white);

            FontMetrics fm = g.getFontMetrics();
            Rectangle2D rect = fm.getStringBounds(this.name, g);
            g.drawString(this.name, (int) (b.x + b.width/2 - rect.getWidth()/2),
                    (int) (b.y + 30/2 + rect.getHeight()/2));

        }
        public void setId(int i) {
            this.id = i;
        }
        public int getId() {
            return  this.id;
        }

        public Point getLocation() {
            Point out = new Point((int) p.getX(),(int) Math.abs(p.getY()  + (25 - r)));
            return out;
        }

        public boolean contains(Point p) {
            return b.contains(p);
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }

        public static void getSelected(List<Node> list, List<Node> selected) {
            selected.clear();
            for (Node n : list) {
                if (n.isSelected()) {
                    selected.add(n);
                }
            }
        }

        public static void selectNone(List<Node> list) {
            for (Node n : list) {
                n.setSelected(false);
            }
        }

        public static boolean selectOne(List<Node> list, Point p) {
            for (Node n : list) {
                if (n.contains(p)) {
                    if (!n.isSelected()) {
                        Node.selectNone(list);
                        n.setSelected(true);
                    }
                    return true;
                }
            }
            return false;
        }

        public static void selectRect(List<Node> list, Rectangle r) {
            for (Node n : list) {
                n.setSelected(r.contains(n.p));
            }
        }

        public static void selectToggle(List<Node> list, Point p) {
            for (Node n : list) {
                if (n.contains(p)) {
                    n.setSelected(!n.isSelected());
                }
            }
        }

        public static void updatePosition(List<Node> list, Point d) {
            for (Node n : list) {
                if (n.isSelected()) {
                    n.p.x += d.x;
                    n.p.y += d.y;
                    n.setBoundary(n.b);
                }
            }
        }

        public static void updateRadius(List<Node> list, int r) {
            for (Node n : list) {
                if (n.isSelected()) {
                    n.r = r;
                    n.setBoundary(n.b);
                }
            }
        }

        public static void updateColor(List<Node> list, Color color) {
            for (Node n : list) {
                if (n.isSelected()) {
                    n.color = color;
                }
            }
        }

        public static void updateName(List<Node> list, String name, int id) {
            for (Node n : list) {
                if (n.isSelected()) {
                    n.name = name;
                    n.id = id;
                }
            }
        }
    }

    private static class ColorIcon implements Icon {

        private static final int WIDE = 20;
        private static final int HIGH = 20;
        private Color color;

        public ColorIcon(Color color) {
            this.color = color;
        }

        public Color getColor() {
            return color;
        }

        public void setColor(Color color) {
            this.color = color;
        }

        public void paintIcon(Component c, Graphics g, int x, int y) {
            g.setColor(color);
            g.fillRect(x, y, WIDE, HIGH);
        }

        public int getIconWidth() {
            return WIDE;
        }

        public int getIconHeight() {
            return HIGH;
        }
    }
}