import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.List;

public class TransportControl {

    // ── Paleta de cores ──────────────────────────────────────────────
    static final Color BG          = new Color(0xF4F6F9);
    static final Color SURFACE     = Color.WHITE;
    static final Color PRIMARY     = new Color(0x1A56DB);
    static final Color PRIMARY_DK  = new Color(0x1043A8);
    static final Color SUCCESS     = new Color(0x0E9F6E);
    static final Color DANGER      = new Color(0xE02424);
    static final Color TEXT_MAIN   = new Color(0x111928);
    static final Color TEXT_MUTED  = new Color(0x6B7280);
    static final Color BORDER_CLR  = new Color(0xE5E7EB);
    static final Color HEADER_BG   = new Color(0x111928);
    static final Color HEADER_FG   = Color.WHITE;
    static final Color ROW_ALT     = new Color(0xF9FAFB);
    static final Color ACCENT      = new Color(0x3F83F8);

    // ── Fontes ────────────────────────────────────────────────────────
    static final Font FONT_TITLE   = new Font("SansSerif", Font.BOLD,   22);
    static final Font FONT_SECTION = new Font("SansSerif", Font.BOLD,   13);
    static final Font FONT_LABEL   = new Font("SansSerif", Font.PLAIN,  12);
    static final Font FONT_INPUT   = new Font("SansSerif", Font.PLAIN,  13);
    static final Font FONT_BTN     = new Font("SansSerif", Font.BOLD,   13);
    static final Font FONT_TABLE_H = new Font("SansSerif", Font.BOLD,   12);
    static final Font FONT_TABLE   = new Font("SansSerif", Font.PLAIN,  12);

    // ── Modelos de dados ──────────────────────────────────────────────
    static final DefaultTableModel vehicleModel = new DefaultTableModel(
            new String[]{"Placa", "Marca / Modelo", "Ano", "Tipo", "Capacidade", "Status"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };

    static final DefaultTableModel driverModel = new DefaultTableModel(
            new String[]{"Nome", "CPF", "Nº CNH", "Categoria", "Telefone", "Status"}, 0) {
        @Override public boolean isCellEditable(int r, int c) { return false; }
    };

    // ── Contadores de ID simples ───────────────────────────────────────
    static int vehicleCount = 0;
    static int driverCount  = 0;

    public static void main(String[] args) {
        try { UIManager.setLookAndFeel(new NimbusLookAndFeel()); }
        catch (Exception ignored) {}

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("TransportControl — Sistema de Gestão de Frota");
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(1100, 720);
            frame.setMinimumSize(new Dimension(900, 600));
            frame.setLocationRelativeTo(null);
            frame.setBackground(BG);

            frame.setLayout(new BorderLayout());
            frame.add(buildHeader(), BorderLayout.NORTH);
            frame.add(buildMain(), BorderLayout.CENTER);
            frame.add(buildStatusBar(), BorderLayout.SOUTH);

            frame.setVisible(true);
        });
    }

    // ── Header ────────────────────────────────────────────────────────
    static JPanel buildHeader() {
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(HEADER_BG);
        header.setBorder(new EmptyBorder(16, 28, 16, 28));

        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 0));
        left.setOpaque(false);

        JLabel icon = new JLabel("🚌");
        icon.setFont(new Font("SansSerif", Font.PLAIN, 22));

        JLabel title = new JLabel("TransportControl");
        title.setFont(FONT_TITLE);
        title.setForeground(HEADER_FG);

        JLabel sub = new JLabel("  Sistema de Gestão de Frota");
        sub.setFont(new Font("SansSerif", Font.PLAIN, 13));
        sub.setForeground(new Color(0x9CA3AF));

        left.add(icon);
        left.add(title);
        left.add(sub);

        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        right.setOpaque(false);

        JLabel dateLabel = new JLabel("Hoje: " + java.time.LocalDate.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        dateLabel.setFont(FONT_LABEL);
        dateLabel.setForeground(new Color(0x9CA3AF));
        right.add(dateLabel);

        header.add(left, BorderLayout.WEST);
        header.add(right, BorderLayout.EAST);
        return header;
    }

    // ── Painel principal com abas ──────────────────────────────────────
    static JTabbedPane buildMain() {
        JTabbedPane tabs = new JTabbedPane(JTabbedPane.TOP);
        tabs.setBackground(BG);
        tabs.setFont(new Font("SansSerif", Font.BOLD, 13));
        tabs.setBorder(new EmptyBorder(4, 8, 0, 8));

        tabs.addTab("  🚗  Veículos  ",  buildVehiclePanel());
        tabs.addTab("  👤  Motoristas  ", buildDriverPanel());
        tabs.addTab("  📋  Painel Geral  ", buildOverviewPanel());

        return tabs;
    }

    // ── Aba: Cadastro de Veículo ──────────────────────────────────────
    static JPanel buildVehiclePanel() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(BG);
        root.setBorder(new EmptyBorder(20, 24, 20, 24));

        // ─ Formulário
        JPanel form = buildCard("Cadastrar Novo Veículo");

        JTextField fPlaca    = styledField("ABC-1234");
        JTextField fMarca    = styledField("Ex: Mercedes-Benz");
        JTextField fModelo   = styledField("Ex: Sprinter 415");
        JTextField fAno      = styledField("2023");
        JTextField fCapac    = styledField("15");
        JComboBox<String> fTipo = styledCombo(
                "Ônibus", "Micro-ônibus", "Van", "Caminhão", "Caminhonete", "Ambulância");
        JComboBox<String> fStatus = styledCombo("Ativo", "Em Manutenção", "Inativo");

        int cols = 3;
        JPanel grid = new JPanel(new GridLayout(0, cols * 2, 10, 12));
        grid.setOpaque(false);

        grid.add(formLabel("Placa *"));      grid.add(fPlaca);
        grid.add(formLabel("Marca *"));      grid.add(fMarca);
        grid.add(formLabel("Modelo *"));     grid.add(fModelo);
        grid.add(formLabel("Ano *"));        grid.add(fAno);
        grid.add(formLabel("Tipo *"));       grid.add(fTipo);
        grid.add(formLabel("Capacidade"));   grid.add(fCapac);
        grid.add(formLabel("Status"));       grid.add(fStatus);

        form.add(grid, BorderLayout.CENTER);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        btnRow.setOpaque(false);
        btnRow.setBorder(new EmptyBorder(16, 0, 0, 0));

        JButton btnSave  = primaryBtn("Cadastrar Veículo");
        JButton btnClear = ghostBtn("Limpar");

        btnRow.add(btnSave);
        btnRow.add(Box.createHorizontalStrut(10));
        btnRow.add(btnClear);
        form.add(btnRow, BorderLayout.SOUTH);

        // ─ Tabela de veículos cadastrados (mini preview)
        JPanel tablePanel = buildCard("Veículos Cadastrados");
        JTable table = buildStyledTable(vehicleModel);
        JScrollPane scroll = styledScroll(table);
        tablePanel.add(scroll, BorderLayout.CENTER);

        // ─ Ação: cadastrar
        btnSave.addActionListener(e -> {
            String placa  = fPlaca.getText().trim();
            String marca  = fMarca.getText().trim();
            String modelo = fModelo.getText().trim();
            String ano    = fAno.getText().trim();

            if (placa.isEmpty() || marca.isEmpty() || modelo.isEmpty() || ano.isEmpty()) {
                showError(root, "Preencha os campos obrigatórios (*).");
                return;
            }
            String marcaModelo = marca + " " + modelo;
            String tipo    = (String) fTipo.getSelectedItem();
            String capac   = fCapac.getText().trim().isEmpty() ? "—" : fCapac.getText().trim();
            String status  = (String) fStatus.getSelectedItem();

            vehicleModel.addRow(new Object[]{
                    placa.toUpperCase(), marcaModelo, ano, tipo, capac, status
            });
            vehicleCount++;
            clearFields(fPlaca, fMarca, fModelo, fAno, fCapac);
            showSuccess(root, "Veículo cadastrado com sucesso!");
        });

        btnClear.addActionListener(e -> clearFields(fPlaca, fMarca, fModelo, fAno, fCapac));

        // ─ Layout da aba
        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, form, tablePanel);
        split.setDividerLocation(300);
        split.setResizeWeight(0.45);
        split.setBorder(null);
        split.setOpaque(false);
        split.setDividerSize(6);

        root.add(split, BorderLayout.CENTER);
        return root;
    }

    // ── Aba: Cadastro de Motorista ────────────────────────────────────
    static JPanel buildDriverPanel() {
        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(BG);
        root.setBorder(new EmptyBorder(20, 24, 20, 24));

        JPanel form = buildCard("Cadastrar Novo Motorista");

        JTextField fNome    = styledField("Nome completo");
        JTextField fCpf     = styledField("000.000.000-00");
        JTextField fCnh     = styledField("Número da CNH");
        JTextField fTel     = styledField("(11) 99999-9999");
        JComboBox<String> fCat    = styledCombo("B", "C", "D", "E", "AB", "AC", "AD", "AE");
        JComboBox<String> fStatus = styledCombo("Ativo", "De Férias", "Afastado", "Inativo");

        JPanel grid = new JPanel(new GridLayout(0, 6, 10, 12));
        grid.setOpaque(false);

        grid.add(formLabel("Nome Completo *")); grid.add(fNome);
        grid.add(formLabel("CPF *"));           grid.add(fCpf);
        grid.add(formLabel("Nº CNH *"));        grid.add(fCnh);
        grid.add(formLabel("Categoria CNH"));   grid.add(fCat);
        grid.add(formLabel("Telefone"));        grid.add(fTel);
        grid.add(formLabel("Status"));          grid.add(fStatus);

        form.add(grid, BorderLayout.CENTER);

        JPanel btnRow = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        btnRow.setOpaque(false);
        btnRow.setBorder(new EmptyBorder(16, 0, 0, 0));

        JButton btnSave  = primaryBtn("Cadastrar Motorista");
        JButton btnClear = ghostBtn("Limpar");

        btnRow.add(btnSave);
        btnRow.add(Box.createHorizontalStrut(10));
        btnRow.add(btnClear);
        form.add(btnRow, BorderLayout.SOUTH);

        JPanel tablePanel = buildCard("Motoristas Cadastrados");
        JTable table = buildStyledTable(driverModel);
        JScrollPane scroll = styledScroll(table);
        tablePanel.add(scroll, BorderLayout.CENTER);

        btnSave.addActionListener(e -> {
            String nome = fNome.getText().trim();
            String cpf  = fCpf.getText().trim();
            String cnh  = fCnh.getText().trim();

            if (nome.isEmpty() || cpf.isEmpty() || cnh.isEmpty()) {
                showError(root, "Preencha os campos obrigatórios (*).");
                return;
            }
            driverModel.addRow(new Object[]{
                    nome, cpf, cnh,
                    (String) fCat.getSelectedItem(),
                    fTel.getText().trim().isEmpty() ? "—" : fTel.getText().trim(),
                    (String) fStatus.getSelectedItem()
            });
            driverCount++;
            clearFields(fNome, fCpf, fCnh, fTel);
            showSuccess(root, "Motorista cadastrado com sucesso!");
        });

        btnClear.addActionListener(e -> clearFields(fNome, fCpf, fCnh, fTel));

        JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, form, tablePanel);
        split.setDividerLocation(280);
        split.setResizeWeight(0.42);
        split.setBorder(null);
        split.setOpaque(false);
        split.setDividerSize(6);

        root.add(split, BorderLayout.CENTER);
        return root;
    }

    // ── Aba: Painel Geral ─────────────────────────────────────────────
    static JPanel buildOverviewPanel() {
        JPanel root = new JPanel(new BorderLayout(0, 12));
        root.setBackground(BG);
        root.setBorder(new EmptyBorder(20, 24, 20, 24));

        // KPI cards
        JPanel kpiRow = new JPanel(new GridLayout(1, 4, 14, 0));
        kpiRow.setOpaque(false);

        kpiRow.add(kpiCard("Total de Veículos",  vehicleModel, null,      PRIMARY,  "🚗"));
        kpiRow.add(kpiCard("Veículos Ativos",    vehicleModel, "Ativo",   SUCCESS,  "✅"));
        kpiRow.add(kpiCard("Total de Motoristas",driverModel,  null,      new Color(0x7C3AED), "👤"));
        kpiRow.add(kpiCard("Motoristas Ativos",  driverModel,  "Ativo",   SUCCESS,  "✅"));

        // Tabelas lado a lado
        JPanel tablesRow = new JPanel(new GridLayout(1, 2, 14, 0));
        tablesRow.setOpaque(false);

        JPanel vPanel = buildCard("Frota de Veículos");
        JTable vTable = buildStyledTable(vehicleModel);
        vPanel.add(styledScroll(vTable), BorderLayout.CENTER);

        JPanel dPanel = buildCard("Motoristas");
        JTable dTable = buildStyledTable(driverModel);
        dPanel.add(styledScroll(dTable), BorderLayout.CENTER);

        tablesRow.add(vPanel);
        tablesRow.add(dPanel);

        root.add(kpiRow,   BorderLayout.NORTH);
        root.add(tablesRow, BorderLayout.CENTER);
        return root;
    }

    // ── KPI Card dinâmico ─────────────────────────────────────────────
    static JPanel kpiCard(String label, DefaultTableModel model,
                          String filterStatus, Color accent, String emoji) {
        JPanel card = new JPanel(new BorderLayout(0, 4));
        card.setBackground(SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_CLR, 1, true),
                new EmptyBorder(18, 20, 18, 20)));

        JLabel emojiLbl = new JLabel(emoji);
        emojiLbl.setFont(new Font("SansSerif", Font.PLAIN, 22));

        JLabel countLbl = new JLabel("0") {
            @Override protected void paintComponent(Graphics g) {
                // refresh count on paint
                int count = 0;
                if (filterStatus == null) {
                    count = model.getRowCount();
                } else {
                    for (int i = 0; i < model.getRowCount(); i++) {
                        Object val = model.getValueAt(i, model.getColumnCount() - 1);
                        if (filterStatus.equals(val)) count++;
                    }
                }
                setText(String.valueOf(count));
                super.paintComponent(g);
            }
        };
        countLbl.setFont(new Font("SansSerif", Font.BOLD, 32));
        countLbl.setForeground(accent);

        JLabel textLbl = new JLabel(label);
        textLbl.setFont(FONT_LABEL);
        textLbl.setForeground(TEXT_MUTED);

        JPanel top = new JPanel(new BorderLayout());
        top.setOpaque(false);
        top.add(emojiLbl, BorderLayout.EAST);
        top.add(countLbl, BorderLayout.WEST);

        card.add(top,    BorderLayout.CENTER);
        card.add(textLbl, BorderLayout.SOUTH);
        return card;
    }

    // ── Status bar ────────────────────────────────────────────────────
    static JPanel buildStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(new Color(0xE5E7EB));
        bar.setBorder(new EmptyBorder(5, 16, 5, 16));

        JLabel info = new JLabel("Sistema de Gestão de Frota  •  v1.0.0");
        info.setFont(new Font("SansSerif", Font.PLAIN, 11));
        info.setForeground(TEXT_MUTED);
        bar.add(info, BorderLayout.WEST);
        return bar;
    }

    // ── Helpers de UI ─────────────────────────────────────────────────

    static JPanel buildCard(String title) {
        JPanel card = new JPanel(new BorderLayout(0, 12));
        card.setBackground(SURFACE);
        card.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_CLR, 1, true),
                new EmptyBorder(18, 20, 18, 20)));

        JLabel lbl = new JLabel(title);
        lbl.setFont(FONT_SECTION);
        lbl.setForeground(TEXT_MAIN);
        lbl.setBorder(new EmptyBorder(0, 0, 10, 0));
        card.add(lbl, BorderLayout.NORTH);
        return card;
    }

    static JTextField styledField(String placeholder) {
        JTextField tf = new JTextField(18);
        tf.setFont(FONT_INPUT);
        tf.setForeground(TEXT_MAIN);
        tf.setBackground(SURFACE);
        tf.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_CLR, 1, true),
                new EmptyBorder(6, 10, 6, 10)));
        tf.setToolTipText(placeholder);
        // light placeholder via focus listener
        tf.setForeground(TEXT_MUTED);
        tf.setText(placeholder);
        tf.addFocusListener(new FocusAdapter() {
            @Override public void focusGained(FocusEvent e) {
                if (tf.getText().equals(placeholder)) { tf.setText(""); tf.setForeground(TEXT_MAIN); }
            }
            @Override public void focusLost(FocusEvent e) {
                if (tf.getText().isEmpty()) { tf.setText(placeholder); tf.setForeground(TEXT_MUTED); }
            }
        });
        return tf;
    }

    static JComboBox<String> styledCombo(String... items) {
        JComboBox<String> cb = new JComboBox<>(items);
        cb.setFont(FONT_INPUT);
        cb.setBackground(SURFACE);
        cb.setForeground(TEXT_MAIN);
        return cb;
    }

    static JLabel formLabel(String text) {
        JLabel l = new JLabel(text);
        l.setFont(FONT_LABEL);
        l.setForeground(TEXT_MUTED);
        return l;
    }

    static JButton primaryBtn(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BTN);
        btn.setBackground(PRIMARY);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.setBorder(new EmptyBorder(10, 22, 10, 22));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(PRIMARY_DK); }
            @Override public void mouseExited(MouseEvent e)  { btn.setBackground(PRIMARY); }
        });
        return btn;
    }

    static JButton ghostBtn(String text) {
        JButton btn = new JButton(text);
        btn.setFont(FONT_BTN);
        btn.setBackground(SURFACE);
        btn.setForeground(TEXT_MUTED);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createCompoundBorder(
                new LineBorder(BORDER_CLR, 1, true),
                new EmptyBorder(9, 18, 9, 18)));
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.addMouseListener(new MouseAdapter() {
            @Override public void mouseEntered(MouseEvent e) { btn.setBackground(BG); }
            @Override public void mouseExited(MouseEvent e)  { btn.setBackground(SURFACE); }
        });
        return btn;
    }

    static JTable buildStyledTable(DefaultTableModel model) {
        JTable table = new JTable(model);
        table.setFont(FONT_TABLE);
        table.setForeground(TEXT_MAIN);
        table.setRowHeight(34);
        table.setShowGrid(false);
        table.setIntercellSpacing(new Dimension(0, 0));
        table.setSelectionBackground(new Color(0xEBF5FF));
        table.setSelectionForeground(TEXT_MAIN);
        table.setFillsViewportHeight(true);
        table.setBackground(SURFACE);

        // Zebra stripes
        table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable t, Object value,
                    boolean sel, boolean foc, int row, int col) {
                super.getTableCellRendererComponent(t, value, sel, foc, row, col);
                setBorder(new EmptyBorder(0, 12, 0, 12));
                setFont(FONT_TABLE);
                if (sel) {
                    setBackground(new Color(0xEBF5FF));
                } else {
                    setBackground(row % 2 == 0 ? SURFACE : ROW_ALT);
                }
                // Color status column
                String text = value != null ? value.toString() : "";
                if (text.equals("Ativo"))          setForeground(SUCCESS);
                else if (text.equals("Inativo") || text.equals("Afastado"))
                                                   setForeground(DANGER);
                else if (text.equals("Em Manutenção") || text.equals("De Férias"))
                                                   setForeground(new Color(0xD97706));
                else                               setForeground(TEXT_MAIN);
                return this;
            }
        });

        JTableHeader header = table.getTableHeader();
        header.setFont(FONT_TABLE_H);
        header.setBackground(new Color(0xF3F4F6));
        header.setForeground(TEXT_MUTED);
        header.setBorder(new MatteBorder(0, 0, 1, 0, BORDER_CLR));
        header.setPreferredSize(new Dimension(0, 36));
        ((DefaultTableCellRenderer) header.getDefaultRenderer())
                .setBorder(new EmptyBorder(0, 12, 0, 12));

        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        return table;
    }

    static JScrollPane styledScroll(JTable table) {
        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(new LineBorder(BORDER_CLR, 1));
        scroll.getViewport().setBackground(SURFACE);
        return scroll;
    }

    static void clearFields(JTextField... fields) {
        for (JTextField f : fields) {
            f.setText("");
            f.setForeground(TEXT_MUTED);
        }
    }

    static void showSuccess(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Sucesso",
                JOptionPane.INFORMATION_MESSAGE);
    }

    static void showError(Component parent, String msg) {
        JOptionPane.showMessageDialog(parent, msg, "Atenção",
                JOptionPane.WARNING_MESSAGE);
    }
}
