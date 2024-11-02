import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.*;
import java.text.ParseException;
import java.util.Arrays;
import java.util.Random;
import java.util.List;
import java.util.ArrayList;
import java.text.SimpleDateFormat;
import java.io.*;
import java.sql.Date;


// Clases de servicio y utilidades
class ConexionBD {
    private static Connection conexion;
    private static final String URL = "jdbc:postgresql://localhost:5432/Gaming_Leagues";
    private static final String USER = "developer";
    private static final String PASSWORD = "23100132";

    public static void conectar() {
        try {
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Conexión establecida con éxito.");
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos: " + e.getMessage());
        }
    }

    public static void desconectar() {
        if (conexion != null) {
            try {
                conexion.close();
                System.out.println("Conexión cerrada con éxito.");
            } catch (SQLException e) {
                System.out.println("Error al cerrar la conexión: " + e.getMessage());
            }
        }
    }

    public static Connection getConexion() {
        return conexion;
    }
}

// Clase principal de la aplicación
public class AplicacionDeportiva {
    private static JFrame frame;
    private static JPanel mainPanel;
    private static JPanel sidebarPanel;
    private static JPanel headerPanel;
    private static CardLayout cardLayout;
    private static JPanel contentPanel;

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> crearYMostrarGUI());
    }

    private static void crearYMostrarGUI() {
        frame = new JFrame("Sistema de Gestión Deportiva");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1280, 720);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);

        // Panel principal con BorderLayout
        mainPanel = new JPanel(new BorderLayout());

        // Barra lateral (Sidebar)
        sidebarPanel = new JPanel(new GridLayout(0, 1));
        sidebarPanel.setPreferredSize(new Dimension(200, 720));
        sidebarPanel.setBackground(new Color(50, 50, 50)); // Color gris oscuro
        sidebarPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, Color.BLACK)); // Línea divisoria negra

        // Personalización de los botones de la barra lateral
        JButton btnInicio = crearBotonSidebar("Inicio");
        JButton btnJugadores = crearBotonSidebar("Jugadores");
        JButton btnEquipos = crearBotonSidebar("Equipos");
        JButton btnLigas = crearBotonSidebar("Ligas");
        JButton btnJuegos = crearBotonSidebar("Juegos");
        JButton btnPartidos = crearBotonSidebar("Partidos");
        JButton btnPartidosEquipos = crearBotonSidebar("Partidos/Equipos");
        JButton btnJugadoresEquipos = crearBotonSidebar("Jugadores/Equipos");
        JButton btnLigasEquipos = crearBotonSidebar("Ligas/Equipos");
        JButton btnProcesos = crearBotonSidebar("Procesos de Negocio");
        JButton btnReportes = crearBotonSidebar("Reportes");


        // Añadir botones a la barra lateral
        sidebarPanel.add(btnInicio);
        sidebarPanel.add(btnJugadores);
        sidebarPanel.add(btnEquipos);
        sidebarPanel.add(btnLigas);
        sidebarPanel.add(btnJuegos);
        sidebarPanel.add(btnPartidos);
        sidebarPanel.add(btnPartidosEquipos);
        sidebarPanel.add(btnJugadoresEquipos);
        sidebarPanel.add(btnLigasEquipos);
        sidebarPanel.add(btnProcesos);
        sidebarPanel.add(btnReportes);


        // Panel superior (Header)
        headerPanel = new JPanel(new BorderLayout());
        headerPanel.setPreferredSize(new Dimension(1280, 50));
        headerPanel.setBackground(new Color(80, 80, 80)); // Gris más claro
        headerPanel.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, Color.BLACK)); // Línea divisoria inferior negra

        JLabel lblTitulo = new JLabel("Gaming_Leagues", JLabel.LEFT);
        lblTitulo.setFont(new Font("Arial", Font.BOLD, 20));
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnCerrarSesion = new JButton("Cerrar Sesión");
        btnCerrarSesion.setBackground(Color.RED);
        btnCerrarSesion.setForeground(Color.WHITE);
        btnCerrarSesion.setFocusPainted(false);
        btnCerrarSesion.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
        btnCerrarSesion.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int confirm = JOptionPane.showConfirmDialog(frame, "¿Seguro que quieres salir?", "Confirmar salida", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    frame.dispose(); // Cerrar la aplicación
                }
            }
        });

        headerPanel.add(lblTitulo, BorderLayout.WEST);
        headerPanel.add(btnCerrarSesion, BorderLayout.EAST);

        // Panel central para mostrar los contenidos de los diferentes paneles
        cardLayout = new CardLayout();
        contentPanel = new JPanel(cardLayout);
        contentPanel.setBackground(Color.WHITE); // Fondo blanco para los contenidos
        contentPanel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY)); // Borde ligero para el contenido

        // Agregar diferentes paneles al contentPanel
        contentPanel.add(new PanelInicio(), "Inicio");
        contentPanel.add(new PanelJugadores(), "Jugadores");
        contentPanel.add(new PanelEquipos(), "Equipos");
        contentPanel.add(new PanelLigas(), "Ligas");
        contentPanel.add(new PanelJuegos(), "Juegos");
        contentPanel.add(new PanelPartidos(), "Partidos");
        contentPanel.add(new PanelPartidosEquipos(), "PartidosEquipos");
        contentPanel.add(new PanelJE(), "JugadoresEquipos");
        contentPanel.add(new PanelLE(), "LigasEquipos");
        contentPanel.add(new PanelProcesos(), "Procesos");
        contentPanel.add(new PanelReportes(), "Reportes");


        // Acción de los botones para cambiar entre los paneles
        btnInicio.addActionListener(e -> cardLayout.show(contentPanel, "Inicio"));
        btnJugadores.addActionListener(e -> cardLayout.show(contentPanel, "Jugadores"));
        btnEquipos.addActionListener(e -> cardLayout.show(contentPanel, "Equipos"));
        btnLigas.addActionListener(e -> cardLayout.show(contentPanel, "Ligas"));
        btnJuegos.addActionListener(e -> cardLayout.show(contentPanel, "Juegos"));
        btnPartidos.addActionListener(e -> cardLayout.show(contentPanel, "Partidos"));
        btnPartidosEquipos.addActionListener(e -> cardLayout.show(contentPanel, "PartidosEquipos"));
        btnJugadoresEquipos.addActionListener(e -> cardLayout.show(contentPanel, "JugadoresEquipos"));
        btnLigasEquipos.addActionListener(e -> cardLayout.show(contentPanel, "LigasEquipos"));
        btnProcesos.addActionListener(e -> cardLayout.show(contentPanel, "Procesos"));
        btnReportes.addActionListener(e -> cardLayout.show(contentPanel, "Reportes"));


        // Añadir los paneles al panel principal
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(sidebarPanel, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);

        // Configurar y mostrar la ventana
        frame.setContentPane(mainPanel);
        frame.setVisible(true);
    }

    // Método para crear un botón de la barra lateral con estilo personalizado
    private static JButton crearBotonSidebar(String texto) {
        JButton boton = new JButton(texto);
        boton.setFocusPainted(false);
        boton.setBackground(new Color(70, 70, 70)); // Gris intermedio
        boton.setForeground(Color.WHITE);
        boton.setFont(new Font("Arial", Font.PLAIN, 16));
        boton.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        boton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(100, 100, 100)); // Cambia color al pasar el ratón
            }

            public void mouseExited(java.awt.event.MouseEvent evt) {
                boton.setBackground(new Color(70, 70, 70)); // Regresa al color original
            }
        });
        return boton;
    }



    // Clase para crear el panel Inicio
    static class PanelInicio extends JPanel {
        public PanelInicio() {
            setLayout(new BorderLayout());
            setBackground(new Color(240, 248, 255)); // Color de fondo claro

            // Información de la aplicación
            JTextArea infoArea = new JTextArea();
            infoArea.setText("Bienvenido a Gaming_Leagues\n\n"
                    + "Gaming_Leagues es una aplicación diseñada para gestionar ligas de videojuegos. "
                    + "Permite crear, modificar y eliminar ligas y equipos, así como llevar un registro "
                    + "de jugadores y sus estadísticas.\n\n"
                    + "Utiliza esta aplicación para organizar tus torneos de videojuegos de manera eficiente.");
            infoArea.setEditable(false);
            infoArea.setMargin(new Insets(10, 10, 10, 10));
            infoArea.setLineWrap(true);
            infoArea.setWrapStyleWord(true);
            infoArea.setBorder(BorderFactory.createTitledBorder("Información de la Aplicación"));
            add(infoArea, BorderLayout.NORTH);

            // Panel de botones
            JPanel buttonPanel = new JPanel();
            buttonPanel.setLayout(new GridLayout(1, 3, 10, 10));
            buttonPanel.setBackground(new Color(240, 248, 255)); // Color de fondo claro

            // Botón para realizar copias de seguridad
            JButton backupButton = new JButton("📦 Realizar Copia de Seguridad");
            backupButton.setBackground(new Color(102, 204, 255)); // Color de fondo del botón
            backupButton.setForeground(Color.WHITE); // Color del texto
            backupButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    realizarCopiasDeSeguridad();
                }
            });

            // Botón para restaurar la base de datos
            JButton restoreButton = new JButton("🔄 Restaurar Base de Datos");
            restoreButton.setBackground(new Color(255, 102, 102)); // Color de fondo del botón
            restoreButton.setForeground(Color.WHITE); // Color del texto
            restoreButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    restaurarBaseDeDatos();
                }
            });

            // Botón adicional (puedes personalizarlo)
            JButton anotherButton = new JButton("⚙️ Configuraciones");
            anotherButton.setBackground(new Color(153, 153, 255)); // Color de fondo del botón
            anotherButton.setForeground(Color.WHITE); // Color del texto
            anotherButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    // Acción para configuraciones
                    JOptionPane.showMessageDialog(PanelInicio.this, "Configuraciones aún no implementadas.", "Información", JOptionPane.INFORMATION_MESSAGE);
                }
            });

            buttonPanel.add(backupButton);
            buttonPanel.add(restoreButton);
            buttonPanel.add(anotherButton);

            add(buttonPanel, BorderLayout.CENTER);

            // Sección de estadísticas (puedes personalizar los datos)
            JPanel statsPanel = new JPanel();
            statsPanel.setLayout(new GridLayout(1, 2, 10, 10));
            statsPanel.setBackground(new Color(240, 248, 255)); // Color de fondo claro
            statsPanel.setBorder(BorderFactory.createTitledBorder("Estadísticas de Ligas"));

            JLabel leaguesLabel = new JLabel("Total de Ligas: 5");
            JLabel teamsLabel = new JLabel("Total de Equipos: 5");
            statsPanel.add(leaguesLabel);
            statsPanel.add(teamsLabel);

            add(statsPanel, BorderLayout.SOUTH);
        }

        // Método para realizar las copias de seguridad
        private void realizarCopiasDeSeguridad() {
            // Directorio donde se guardarán las copias de seguridad
            File backupDir = new File("backups/");
            if (!backupDir.exists()) {
                backupDir.mkdir(); // Crear el directorio si no existe
            }

            // Nombre del archivo de respaldo
            String timestamp = String.valueOf(System.currentTimeMillis());
            String backupFile = "backups/respaldodb_" + timestamp + ".sql";

            // Ruta completa al ejecutable pg_dump
            String pgDumpPath = "C:\\Program Files\\PostgreSQL\\16\\bin\\pg_dump.exe"; // Ajusta la versión y la ruta según tu instalación

            // Comando para crear la copia de seguridad de la base de datos en PostgreSQL
            List<String> command = Arrays.asList(
                    pgDumpPath,
                    "-U", "developer",
                    "-h", "localhost",
                    "-d", "Gaming_Leagues",
                    "-f", backupFile
            );

            try {
                ProcessBuilder processBuilder = new ProcessBuilder(command);
                processBuilder.environment().put("PGPASSWORD", "23100132"); // Contraseña de la base de datos
                processBuilder.redirectErrorStream(true); // Redirigir el error al flujo de salida

                Process process = processBuilder.start();

                // Captura la salida del proceso
                BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

                // Leer la salida estándar
                String s;
                while ((s = stdInput.readLine()) != null) {
                    System.out.println(s);
                }

                int exitCode = process.waitFor();

                // Verificación de que la copia de seguridad se haya creado
                if (exitCode == 0 && new File(backupFile).exists()) {
                    JOptionPane.showMessageDialog(this, "Copia de seguridad creada exitosamente: " + backupFile, "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Error al crear la copia de seguridad. Código de salida: " + exitCode, "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException | InterruptedException e) {
                JOptionPane.showMessageDialog(this, "Error durante la creación de la copia de seguridad: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        // Método para restaurar la base de datos
        private void restaurarBaseDeDatos() {
            // Usar JFileChooser para seleccionar el archivo de respaldo
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setDialogTitle("Seleccionar archivo de respaldo");

            // Abrir el cuadro de diálogo y esperar la selección
            int userSelection = fileChooser.showOpenDialog(this);

            if (userSelection != JFileChooser.APPROVE_OPTION) {
                JOptionPane.showMessageDialog(this, "Proceso de restauración cancelado.", "Información", JOptionPane.INFORMATION_MESSAGE);
                return; // El usuario canceló la selección
            }

            File backupFile = fileChooser.getSelectedFile();

            // Ruta completa al ejecutable psql
            String psqlPath = "C:\\Program Files\\PostgreSQL\\16\\bin\\psql.exe"; // Ajusta la versión y la ruta según tu instalación

            // Comando para restaurar la base de datos usando psql
            List<String> command = Arrays.asList(
                    psqlPath,
                    "-U", "developer",
                    "-h", "localhost",
                    "-d", "Gaming_Leagues",
                    "-f", backupFile.getAbsolutePath() // Ruta al archivo de respaldo
            );

            try {
                ProcessBuilder processBuilder = new ProcessBuilder(command);
                processBuilder.environment().put("PGPASSWORD", "23100132"); // Contraseña de la base de datos
                processBuilder.redirectErrorStream(true); // Redirigir el error al flujo de salida

                Process process = processBuilder.start();

                // Captura la salida del proceso
                BufferedReader stdInput = new BufferedReader(new InputStreamReader(process.getInputStream()));

                // Leer la salida estándar
                String s;
                StringBuilder output = new StringBuilder();
                while ((s = stdInput.readLine()) != null) {
                    output.append(s).append("\n");
                    System.out.println(s);
                }

                int exitCode = process.waitFor();

                // Verificación de que la restauración se haya completado
                if (exitCode == 0) {
                    JOptionPane.showMessageDialog(this, "Base de datos restaurada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Error al restaurar la base de datos. Código de salida: " + exitCode + "\nSalida:\n" + output.toString(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (IOException | InterruptedException e) {
                JOptionPane.showMessageDialog(this, "Error durante la restauración de la base de datos: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

        // Clase para crear el panel de jugadores
    static class PanelJugadores extends JPanel {
        private JTable jugadoresTable;
        private DefaultTableModel tableModel;
        private Connection connection;

        public PanelJugadores() {
            setLayout(new BorderLayout());

            // Panel para la tabla de juegos
            JPanel panelTabla = new JPanel(new BorderLayout());
            panelTabla.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE),
                    "Lista de Jugadores",
                    TitledBorder.LEFT,
                    TitledBorder.TOP,
                    new Font("Arial", Font.BOLD, 14),
                    Color.BLUE));
            panelTabla.setBackground(Color.LIGHT_GRAY);


            // Crear tabla para mostrar jugadores
            tableModel = new DefaultTableModel();
            tableModel.addColumn("ID");
            tableModel.addColumn("Nombre");
            tableModel.addColumn("Apellido");
            tableModel.addColumn("Género");
            tableModel.addColumn("Dirección");

            jugadoresTable = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(jugadoresTable);
            add(scrollPane, BorderLayout.CENTER);
            panelTabla.add(scrollPane, BorderLayout.CENTER);

            add(panelTabla, BorderLayout.CENTER);

            // Panel para agregar nuevos jugadores
            JPanel panelAgregar = new JPanel();
            panelAgregar.setLayout(new GridBagLayout());
            panelAgregar.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE),
                    "Agregar/Modificar Equipo",
                    TitledBorder.LEFT,
                    TitledBorder.TOP,
                    new Font("Arial", Font.BOLD, 14),
                    Color.BLUE));
            panelAgregar.setBackground(Color.LIGHT_GRAY);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5); // Espaciado entre componentes
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Campos de entrada
            JTextField nombreField = new JTextField(15);
            JTextField apellidoField = new JTextField(15);
            JTextField generoField = new JTextField(15);
            JTextField direccionField = new JTextField(15);

            // Agregar etiquetas y campos al panel
            gbc.gridx = 0; gbc.gridy = 0; panelAgregar.add(new JLabel("Nombre:"), gbc);
            gbc.gridx = 1; gbc.gridy = 0; panelAgregar.add(nombreField, gbc);
            gbc.gridx = 0; gbc.gridy = 1; panelAgregar.add(new JLabel("Apellido:"), gbc);
            gbc.gridx = 1; gbc.gridy = 1; panelAgregar.add(apellidoField, gbc);
            gbc.gridx = 0; gbc.gridy = 2; panelAgregar.add(new JLabel("Género:"), gbc);
            gbc.gridx = 1; gbc.gridy = 2; panelAgregar.add(generoField, gbc);
            gbc.gridx = 0; gbc.gridy = 3; panelAgregar.add(new JLabel("Dirección:"), gbc);
            gbc.gridx = 1; gbc.gridy = 3; panelAgregar.add(direccionField, gbc);

            // Panel para botones
            JPanel panelBotones = new JPanel();
            panelBotones.setLayout(new FlowLayout());

            // Botón para agregar jugador
            JButton agregarButton = new JButton("Agregar");
            agregarButton.addActionListener(e -> {
                String nombre = nombreField.getText();
                String apellido = apellidoField.getText();
                String genero = generoField.getText();
                String direccion = direccionField.getText();
                agregarJugador(nombre, apellido, genero, direccion);
            });
            panelBotones.add(agregarButton);

            // Botón para modificar jugador
            JButton modificarButton = new JButton("Modificar");
            modificarButton.addActionListener(e -> {
                int selectedRow = jugadoresTable.getSelectedRow();
                if (selectedRow != -1) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    String nombre = nombreField.getText();
                    String apellido = apellidoField.getText();
                    String genero = generoField.getText();
                    String direccion = direccionField.getText();
                    modificarJugador(id, nombre, apellido, genero, direccion);
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione un jugador para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            });
            panelBotones.add(modificarButton);

            // Botón para eliminar jugador
            JButton eliminarButton = new JButton("Eliminar");
            eliminarButton.addActionListener(e -> {
                int selectedRow = jugadoresTable.getSelectedRow();
                if (selectedRow != -1) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    eliminarJugador(id);
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione un jugador para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            });
            panelBotones.add(eliminarButton);

            // Añadir panel de botones al panel de agregar
            gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; panelAgregar.add(panelBotones, gbc);

            add(panelAgregar, BorderLayout.SOUTH);

            // Establecer conexión a la base de datos
            ConexionBD.conectar();
            connection = ConexionBD.getConexion();
            if (connection != null) {
                cargarJugadores();
            } else {
                JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // Métodos para cargar, agregar, modificar y eliminar jugadores (se mantienen igual)
        private void cargarJugadores() {
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM Players");
                while (resultSet.next()) {
                    int id = resultSet.getInt("player_id");
                    String nombre = resultSet.getString("first_name");
                    String apellido = resultSet.getString("last_name");
                    String genero = resultSet.getString("gender");
                    String direccion = resultSet.getString("address");

                    tableModel.addRow(new Object[]{id, nombre, apellido, genero, direccion});
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al cargar jugadores desde la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        private void agregarJugador(String nombre, String apellido, String genero, String direccion) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO Players (first_name, last_name, gender, address) VALUES (?, ?, ?, ?)"
                );
                preparedStatement.setString(1, nombre);
                preparedStatement.setString(2, apellido);
                preparedStatement.setString(3, genero);
                preparedStatement.setString(4, direccion);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Jugador agregado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    tableModel.setRowCount(0); // Limpiar la tabla antes de cargar los datos actualizados
                    cargarJugadores(); // Volver a cargar los jugadores desde la base de datos
                } else {
                    JOptionPane.showMessageDialog(this, "Error al agregar el jugador.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al agregar el jugador.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        private void modificarJugador(int id, String nombre, String apellido, String genero, String direccion) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE Players SET first_name = ?, last_name = ?, gender = ?, address = ? WHERE player_id = ?"
                );
                preparedStatement.setString(1, nombre);
                preparedStatement.setString(2, apellido);
                preparedStatement.setString(3, genero);
                preparedStatement.setString(4, direccion);
                preparedStatement.setInt(5, id);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Jugador modificado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    tableModel.setRowCount(0); // Limpiar la tabla antes de cargar los datos actualizados
                    cargarJugadores(); // Volver a cargar los jugadores desde la base de datos
                } else {
                    JOptionPane.showMessageDialog(this, "Error al modificar el jugador.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al modificar el jugador.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        private void eliminarJugador(int id) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "DELETE FROM Players WHERE player_id = ?"
                );
                preparedStatement.setInt(1, id);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Jugador eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    tableModel.setRowCount(0); // Limpiar la tabla antes de cargar los datos actualizados
                    cargarJugadores(); // Volver a cargar los jugadores desde la base de datos
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar el jugador.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar el jugador.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }

    // Clase para crear el panel de equipos
    static class PanelEquipos extends JPanel {
        private JTable equiposTable;
        private DefaultTableModel tableModel;
        private Connection connection;

        public PanelEquipos() {
            setLayout(new BorderLayout());

            // Panel para la tabla de juegos
            JPanel panelTabla = new JPanel(new BorderLayout());
            panelTabla.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE),
                    "Lista de Equipos",
                    TitledBorder.LEFT,
                    TitledBorder.TOP,
                    new Font("Arial", Font.BOLD, 14),
                    Color.BLUE));
            panelTabla.setBackground(Color.LIGHT_GRAY);


            // Crear tabla para mostrar equipos
            tableModel = new DefaultTableModel();
            tableModel.addColumn("ID");
            tableModel.addColumn("Nombre");
            tableModel.addColumn("Creado por");
            tableModel.addColumn("Fecha de Creación");
            tableModel.addColumn("Fecha de Disolución");

            equiposTable = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(equiposTable);
            add(scrollPane, BorderLayout.CENTER);
            panelTabla.add(scrollPane, BorderLayout.CENTER);

            add(panelTabla, BorderLayout.CENTER);

            // Panel para agregar o modificar equipos
            JPanel panelAgregar = new JPanel();
            panelAgregar.setLayout(new GridBagLayout());
            panelAgregar.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.BLUE),
                    "Agregar/Modificar Equipo",
                    TitledBorder.LEFT,
                    TitledBorder.TOP,
                    new Font("Arial", Font.BOLD, 14),
                    Color.BLUE));
            panelAgregar.setBackground(Color.LIGHT_GRAY);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5); // Espaciado entre componentes
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Campos de entrada
            JTextField nombreField = new JTextField(15);
            JTextField creadoPorField = new JTextField(15);
            JTextField fechaCreacionField = new JTextField(15);
            JTextField fechaDisolucionField = new JTextField(15);

            // Agregar etiquetas y campos al panel
            gbc.gridx = 0; gbc.gridy = 0; panelAgregar.add(new JLabel("Nombre:"), gbc);
            gbc.gridx = 1; gbc.gridy = 0; panelAgregar.add(nombreField, gbc);
            gbc.gridx = 0; gbc.gridy = 1; panelAgregar.add(new JLabel("Creado por el jugador:"), gbc);
            gbc.gridx = 1; gbc.gridy = 1; panelAgregar.add(creadoPorField, gbc);
            gbc.gridx = 0; gbc.gridy = 2; panelAgregar.add(new JLabel("Fecha de Creación:"), gbc);
            gbc.gridx = 1; gbc.gridy = 2; panelAgregar.add(fechaCreacionField, gbc);
            gbc.gridx = 0; gbc.gridy = 3; panelAgregar.add(new JLabel("Fecha de Disolución:"), gbc);
            gbc.gridx = 1; gbc.gridy = 3; panelAgregar.add(fechaDisolucionField, gbc);

            // Panel para botones
            JPanel panelBotones = new JPanel();
            panelBotones.setLayout(new FlowLayout());

            // Botón para agregar equipo
            JButton agregarButton = new JButton("Agregar");
            agregarButton.addActionListener(e -> {
                String nombre = nombreField.getText();
                int creadoPor = Integer.parseInt(creadoPorField.getText());
                String fechaCreacion = fechaCreacionField.getText();
                String fechaDisolucion = fechaDisolucionField.getText();
                agregarEquipo(nombre, creadoPor, fechaCreacion, fechaDisolucion);
            });
            panelBotones.add(agregarButton);

            // Botón para modificar equipo
            JButton modificarButton = new JButton("Modificar");
            modificarButton.addActionListener(e -> {
                int selectedRow = equiposTable.getSelectedRow();
                if (selectedRow != -1) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    String nombre = nombreField.getText();
                    int creadoPor = Integer.parseInt(creadoPorField.getText());
                    String fechaCreacion = fechaCreacionField.getText();
                    String fechaDisolucion = fechaDisolucionField.getText();
                    modificarEquipo(id, nombre, creadoPor, fechaCreacion, fechaDisolucion);
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione un equipo para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            });
            panelBotones.add(modificarButton);

            // Botón para eliminar equipo
            JButton eliminarButton = new JButton("Eliminar");
            eliminarButton.addActionListener(e -> {
                int selectedRow = equiposTable.getSelectedRow();
                if (selectedRow != -1) {
                    int id = (int) tableModel.getValueAt(selectedRow, 0);
                    eliminarEquipo(id);
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione un equipo para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            });
            panelBotones.add(eliminarButton);

            // Añadir panel de botones al panel de agregar
            gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; panelAgregar.add(panelBotones, gbc);

            add(panelAgregar, BorderLayout.SOUTH);

            // Conectar a la base de datos y cargar datos
            ConexionBD.conectar();
            connection = ConexionBD.getConexion();
            if (connection != null) {
                cargarEquipos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }



    // Método para cargar los equipos desde la base de datos a la tabla
        private void cargarEquipos() {
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM Teams");
                tableModel.setRowCount(0); // Limpiar la tabla antes de cargar los datos
                while (resultSet.next()) {
                    int id = resultSet.getInt("team_id");
                    String nombre = resultSet.getString("team_name");
                    int creadoPor = resultSet.getInt("created_by_player_id");
                    String fechaCreacion = resultSet.getString("date_created");
                    String fechaDisolucion = resultSet.getString("date_disbanded");

                    tableModel.addRow(new Object[]{id, nombre, creadoPor, fechaCreacion, fechaDisolucion});
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al cargar equipos desde la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        private void agregarEquipo(String nombre, int creadoPor, String fechaCreacion, String fechaDisolucion) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO Teams (team_name, created_by_player_id, date_created, date_disbanded) VALUES (?, ?, ?, ?)"
                );
                preparedStatement.setString(1, nombre);
                preparedStatement.setInt(2, creadoPor);
                preparedStatement.setDate(3, java.sql.Date.valueOf(fechaCreacion)); // Conversión a fecha
                preparedStatement.setDate(4, (fechaDisolucion.isEmpty() ? null : java.sql.Date.valueOf(fechaDisolucion))); // Conversión o null

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Equipo agregado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    tableModel.setRowCount(0); // Limpiar la tabla antes de cargar los datos actualizados
                    cargarEquipos(); // Volver a cargar los equipos desde la base de datos
                } else {
                    JOptionPane.showMessageDialog(this, "Error al agregar el equipo.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al agregar el equipo.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        private void modificarEquipo(int id, String nombre, int creadoPor, String fechaCreacion, String fechaDisolucion) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE Teams SET team_name = ?, created_by_player_id = ?, date_created = ?, date_disbanded = ? WHERE team_id = ?"
                );
                preparedStatement.setString(1, nombre);
                preparedStatement.setInt(2, creadoPor);
                preparedStatement.setDate(3, java.sql.Date.valueOf(fechaCreacion)); // Conversión a fecha
                preparedStatement.setDate(4, (fechaDisolucion.isEmpty() ? null : java.sql.Date.valueOf(fechaDisolucion))); // Conversión o null
                preparedStatement.setInt(5, id);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Equipo modificado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarEquipos(); // Volver a cargar los equipos actualizados
                } else {
                    JOptionPane.showMessageDialog(this, "Error al modificar el equipo.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al modificar el equipo.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }


        // Método para eliminar un equipo de la base de datos y actualizar la tabla
        private void eliminarEquipo(int id) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "DELETE FROM Teams WHERE team_id = ?"
                );
                preparedStatement.setInt(1, id);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Equipo eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    tableModel.setRowCount(0); // Limpiar la tabla antes de cargar los datos actualizados
                    cargarEquipos(); // Volver a cargar los equipos desde la base de datos
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar el equipo.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar el equipo.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }


    // Clase para crear el panel de ligas
    static class PanelLigas extends JPanel {
        private JTable ligasTable;
        private DefaultTableModel tableModel;
        private Connection connection;
        private int ligaSeleccionadaId = -1; // ID de la liga seleccionada para modificación

        public PanelLigas() {
            setLayout(new BorderLayout());

            // Panel para la tabla de juegos
            JPanel panelTabla = new JPanel(new BorderLayout());
            panelTabla.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE),
                    "Lista de Ligas",
                    TitledBorder.LEFT,
                    TitledBorder.TOP,
                    new Font("Arial", Font.BOLD, 14),
                    Color.BLUE));
            panelTabla.setBackground(Color.LIGHT_GRAY);


            // Crear tabla para mostrar ligas
            tableModel = new DefaultTableModel();
            tableModel.addColumn("ID");
            tableModel.addColumn("Nombre");
            tableModel.addColumn("Detalles");

            ligasTable = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(ligasTable);
            add(scrollPane, BorderLayout.CENTER);
            panelTabla.add(scrollPane, BorderLayout.CENTER);

            add(panelTabla, BorderLayout.CENTER);

            // Panel para agregar nuevas ligas
            JPanel panelAgregar = new JPanel();
            panelAgregar.setLayout(new GridBagLayout());
            panelAgregar.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE),
                    "Agregar/Modificar Liga",
                    TitledBorder.LEFT,
                    TitledBorder.TOP,
                    new Font("Arial", Font.BOLD, 14),
                    Color.BLUE));
            panelAgregar.setBackground(Color.LIGHT_GRAY);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Campos de entrada
            JTextField nombreField = new JTextField(15);
            JTextField detallesField = new JTextField(15);

            // Agregar etiquetas y campos al panel
            gbc.gridx = 0; gbc.gridy = 0; panelAgregar.add(new JLabel("Nombre:"), gbc);
            gbc.gridx = 1; gbc.gridy = 0; panelAgregar.add(nombreField, gbc);
            gbc.gridx = 0; gbc.gridy = 1; panelAgregar.add(new JLabel("Detalles:"), gbc);
            gbc.gridx = 1; gbc.gridy = 1; panelAgregar.add(detallesField, gbc);

            // Panel para botones
            JPanel panelBotones = new JPanel();
            panelBotones.setLayout(new FlowLayout());

            // Botón para agregar liga
            JButton agregarButton = new JButton("Agregar");
            agregarButton.addActionListener(e -> {
                String nombre = nombreField.getText();
                String detalles = detallesField.getText();
                agregarLiga(nombre, detalles);
            });
            panelBotones.add(agregarButton);

            // Botón para modificar liga
            JButton modificarButton = new JButton("Modificar");
            modificarButton.addActionListener(e -> {
                int selectedRow = ligasTable.getSelectedRow();
                if (selectedRow != -1) {
                    ligaSeleccionadaId = (int) tableModel.getValueAt(selectedRow, 0);
                    String nombre = nombreField.getText();
                    String detalles = detallesField.getText();
                    modificarLiga(ligaSeleccionadaId, nombre, detalles);
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione una liga para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            });
            panelBotones.add(modificarButton);

            // Botón para eliminar liga
            JButton eliminarButton = new JButton("Eliminar");
            eliminarButton.addActionListener(e -> {
                int selectedRow = ligasTable.getSelectedRow();
                if (selectedRow != -1) {
                    int ligaId = (int) tableModel.getValueAt(selectedRow, 0);
                    eliminarLiga(ligaId);
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione una liga para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            });
            panelBotones.add(eliminarButton);

            // Añadir panel de botones al panel de agregar
            gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; panelAgregar.add(panelBotones, gbc);

            add(panelAgregar, BorderLayout.SOUTH);

            // Establecer conexión a la base de datos
            ConexionBD.conectar();
            connection = ConexionBD.getConexion();
            if (connection != null) {
                cargarLigas();
            } else {
                JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // Método para cargar las ligas desde la base de datos a la tabla
        private void cargarLigas() {
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM Leagues");
                tableModel.setRowCount(0); // Limpiar la tabla antes de cargar los datos
                while (resultSet.next()) {
                    int id = resultSet.getInt("league_id");
                    String nombre = resultSet.getString("league_name");
                    String detalles = resultSet.getString("league_details");

                    tableModel.addRow(new Object[]{id, nombre, detalles});
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al cargar ligas desde la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        // Método para agregar una nueva liga a la base de datos y actualizar la tabla
        private void agregarLiga(String nombre, String detalles) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO Leagues (league_name, league_details) VALUES (?, ?)"
                );
                preparedStatement.setString(1, nombre);
                preparedStatement.setString(2, detalles);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Liga agregada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    tableModel.setRowCount(0); // Limpiar la tabla antes de cargar los datos actualizados
                    cargarLigas(); // Volver a cargar las ligas desde la base de datos
                } else {
                    JOptionPane.showMessageDialog(this, "Error al agregar la liga.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al agregar la liga.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        // Método para modificar una liga en la base de datos y actualizar la tabla
        private void modificarLiga(int id, String nombre, String detalles) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE Leagues SET league_name = ?, league_details = ? WHERE league_id = ?"
                );
                preparedStatement.setString(1, nombre);
                preparedStatement.setString(2, detalles);
                preparedStatement.setInt(3, id);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Liga modificada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    tableModel.setRowCount(0); // Limpiar la tabla antes de cargar los datos actualizados
                    cargarLigas(); // Volver a cargar las ligas desde la base de datos
                } else {
                    JOptionPane.showMessageDialog(this, "Error al modificar la liga.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al modificar la liga.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        // Método para eliminar una liga de la base de datos y actualizar la tabla
        private void eliminarLiga(int id) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "DELETE FROM Leagues WHERE league_id = ?"
                );
                preparedStatement.setInt(1, id);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Liga eliminada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    tableModel.setRowCount(0); // Limpiar la tabla antes de cargar los datos actualizados
                    cargarLigas(); // Volver a cargar las ligas desde la base de datos
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar la liga.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar la liga.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        // Método para cerrar la conexión a la base de datos
        private void cerrarConexion() {
            try {
                if (connection != null && !connection.isClosed()) {
                    connection.close();
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    // Clase para crear el panel de juegos
    static class PanelJuegos extends JPanel {
        private JTable juegosTable;
        private DefaultTableModel tableModel;
        private Connection connection;
        private JTextField nombreField;
        private JTextField descripcionField;
        private int juegoSeleccionadoId = -1; // ID del juego seleccionado para modificación

        public PanelJuegos() {
            setLayout(new BorderLayout());

            // Panel para la tabla de juegos
            JPanel panelTabla = new JPanel(new BorderLayout());
            panelTabla.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE),
                    "Lista de Juegos",
                    TitledBorder.LEFT,
                    TitledBorder.TOP,
                    new Font("Arial", Font.BOLD, 14),
                    Color.BLUE));
            panelTabla.setBackground(Color.LIGHT_GRAY);


            // Crear tabla para mostrar juegos
            tableModel = new DefaultTableModel();
            tableModel.addColumn("Código");
            tableModel.addColumn("Nombre");
            tableModel.addColumn("Descripción");
            juegosTable = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(juegosTable);
            panelTabla.add(scrollPane, BorderLayout.CENTER);

            add(panelTabla, BorderLayout.CENTER);

            // Panel para agregar y modificar juegos
            JPanel panelAgregar = new JPanel(new GridBagLayout());
            panelAgregar.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE),
                    "Agregar/Modificar Juego",
                    TitledBorder.LEFT,
                    TitledBorder.TOP,
                    new Font("Arial", Font.BOLD, 14),
                    Color.BLUE));
            panelAgregar.setBackground(Color.LIGHT_GRAY);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Nombre del juego
            gbc.gridx = 0;
            gbc.gridy = 0;
            panelAgregar.add(new JLabel("Nombre:"), gbc);

            nombreField = new JTextField(15);
            gbc.gridx = 1;
            panelAgregar.add(nombreField, gbc);

            // Descripción del juego
            gbc.gridx = 0;
            gbc.gridy = 1;
            panelAgregar.add(new JLabel("Descripción:"), gbc);

            descripcionField = new JTextField(15);
            gbc.gridx = 1;
            panelAgregar.add(descripcionField, gbc);

            // Panel para botones
            JPanel panelBotones = new JPanel();
            panelBotones.setLayout(new FlowLayout());

            // Botón Agregar
            JButton agregarButton = new JButton("Agregar");
            agregarButton.addActionListener(e -> {
                String nombre = nombreField.getText();
                String descripcion = descripcionField.getText();
                agregarJuego(nombre, descripcion);
            });
            panelBotones.add(agregarButton);

            // Botón Modificar
            JButton modificarButton = new JButton("Modificar");
            modificarButton.addActionListener(e -> {
                int selectedRow = juegosTable.getSelectedRow();
                if (selectedRow != -1) {
                    juegoSeleccionadoId = (int) tableModel.getValueAt(selectedRow, 0);
                    String nuevoNombre = nombreField.getText();
                    String nuevaDescripcion = descripcionField.getText();
                    modificarJuego(juegoSeleccionadoId, nuevoNombre, nuevaDescripcion);
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione un juego para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            });
            panelBotones.add(modificarButton);

            // Botón Eliminar
            JButton eliminarButton = new JButton("Eliminar");
            eliminarButton.addActionListener(e -> {
                int selectedRow = juegosTable.getSelectedRow();
                if (selectedRow != -1) {
                    int juegoId = (int) tableModel.getValueAt(selectedRow, 0);
                    eliminarJuego(juegoId);
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione un juego para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            });
            panelBotones.add(eliminarButton);

            // Añadir panel de botones al panel de agregar
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            panelAgregar.add(panelBotones, gbc);

            add(panelAgregar, BorderLayout.SOUTH);

            // Establecer conexión a la base de datos
            ConexionBD.conectar();
            connection = ConexionBD.getConexion();
            if (connection != null) {
                cargarJuegos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // Método para cargar los juegos desde la base de datos a la tabla
        private void cargarJuegos() {
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM Games");
                tableModel.setRowCount(0); // Limpiar la tabla antes de cargar los datos
                while (resultSet.next()) {
                    int codigo = resultSet.getInt("game_code");
                    String nombre = resultSet.getString("game_name");
                    String descripcion = resultSet.getString("game_description");
                    tableModel.addRow(new Object[]{codigo, nombre, descripcion});
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al cargar juegos desde la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        // Método para agregar un nuevo juego a la base de datos y actualizar la tabla
        private void agregarJuego(String nombre, String descripcion) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO Games (game_name, game_description) VALUES (?, ?)"
                );
                preparedStatement.setString(1, nombre);
                preparedStatement.setString(2, descripcion);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Juego agregado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    tableModel.setRowCount(0); // Limpiar la tabla antes de cargar los datos actualizados
                    cargarJuegos(); // Volver a cargar los juegos desde la base de datos
                } else {
                    JOptionPane.showMessageDialog(this, "Error al agregar el juego.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al agregar el juego.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        // Método para modificar un juego en la base de datos y actualizar la tabla
        private void modificarJuego(int codigo, String nombre, String descripcion) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE Games SET game_name = ?, game_description = ? WHERE game_code = ?"
                );
                preparedStatement.setString(1, nombre);
                preparedStatement.setString(2, descripcion);
                preparedStatement.setInt(3, codigo);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Juego modificado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    tableModel.setRowCount(0); // Limpiar la tabla antes de cargar los datos actualizados
                    cargarJuegos(); // Volver a cargar los juegos desde la base de datos
                } else {
                    JOptionPane.showMessageDialog(this, "Error al modificar el juego.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al modificar el juego.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        // Método para eliminar un juego de la base de datos y actualizar la tabla
        private void eliminarJuego(int codigo) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "DELETE FROM Games WHERE game_code = ?"
                );
                preparedStatement.setInt(1, codigo);
                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Juego eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    tableModel.setRowCount(0); // Limpiar la tabla antes de cargar los datos actualizados
                    cargarJuegos(); // Volver a cargar los juegos desde la base de datos
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar el juego.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar el juego.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }


    // Clase para crear el panel de partidos entre jugadores
    static class PanelPartidos extends JPanel {
        private JTable partidosTable;
        private JTable rankingsTable;
        private DefaultTableModel partidosTableModel;
        private DefaultTableModel rankingsTableModel;
        private Connection connection;
        private int partidoSeleccionadoId = -1; // ID del partido seleccionado para modificación

        public PanelPartidos() {
            setLayout(new BorderLayout());

            // Panel para la tabla de juegos
            JPanel panelTablas = new JPanel(new GridLayout(2, 1)); // GridLayout para tablas de partidos y rankings
            panelTablas.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE),
                    "Lista de Partidos y Rankings",
                    TitledBorder.LEFT,
                    TitledBorder.TOP,
                    new Font("Arial", Font.BOLD, 14),
                    Color.BLUE));
            panelTablas.setBackground(Color.LIGHT_GRAY);

            // Crear tabla para mostrar partidos
            partidosTableModel = new DefaultTableModel();
            partidosTableModel.addColumn("ID");
            partidosTableModel.addColumn("Código de Juego");
            partidosTableModel.addColumn("Jugador 1");
            partidosTableModel.addColumn("Jugador 2");
            partidosTableModel.addColumn("Fecha");
            partidosTableModel.addColumn("Resultado");

            partidosTable = new JTable(partidosTableModel);
            JScrollPane scrollPanePartidos = new JScrollPane(partidosTable);
            panelTablas.add(scrollPanePartidos);

            // Crear tabla para mostrar rankings
            rankingsTableModel = new DefaultTableModel();
            rankingsTableModel.addColumn("ID Jugador");
            rankingsTableModel.addColumn("Nombre Jugador");
            rankingsTableModel.addColumn("Game Code");
            rankingsTableModel.addColumn("Ranking");

            rankingsTable = new JTable(rankingsTableModel);
            JScrollPane scrollPaneRankings = new JScrollPane(rankingsTable);
            panelTablas.add(scrollPaneRankings);

            add(panelTablas, BorderLayout.CENTER);

            // Panel para agregar/modificar partidos con estilo personalizado
            JPanel panelAgregar = new JPanel(new GridBagLayout());
            panelAgregar.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.BLUE),
                    "Agregar/Modificar Partido",
                    TitledBorder.LEFT,
                    TitledBorder.TOP,
                    new Font("Arial", Font.BOLD, 14),
                    Color.BLUE
            ));
            panelAgregar.setBackground(Color.LIGHT_GRAY);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Campos de entrada
            JTextField codigoJuegoField = new JTextField(15);
            JTextField jugador1Field = new JTextField(15);
            JTextField jugador2Field = new JTextField(15);
            JTextField fechaField = new JTextField(15);
            JTextField resultadoField = new JTextField(15);

            // Agregar etiquetas y campos al panel
            gbc.gridx = 0; gbc.gridy = 0; panelAgregar.add(new JLabel("Código de Juego:"), gbc);
            gbc.gridx = 1; gbc.gridy = 0; panelAgregar.add(codigoJuegoField, gbc);

            gbc.gridx = 0; gbc.gridy = 1; panelAgregar.add(new JLabel("Jugador 1:"), gbc);
            gbc.gridx = 1; gbc.gridy = 1; panelAgregar.add(jugador1Field, gbc);

            gbc.gridx = 0; gbc.gridy = 2; panelAgregar.add(new JLabel("Jugador 2:"), gbc);
            gbc.gridx = 1; gbc.gridy = 2; panelAgregar.add(jugador2Field, gbc);

            gbc.gridx = 0; gbc.gridy = 3; panelAgregar.add(new JLabel("Fecha:"), gbc);
            gbc.gridx = 1; gbc.gridy = 3; panelAgregar.add(fechaField, gbc);

            gbc.gridx = 0; gbc.gridy = 4; panelAgregar.add(new JLabel("Resultado:"), gbc);
            gbc.gridx = 1; gbc.gridy = 4; panelAgregar.add(resultadoField, gbc);

            // Panel para botones
            JPanel panelBotones = new JPanel(new FlowLayout());

            // Botón para agregar partido
            JButton agregarButton = new JButton("Agregar");
            agregarButton.addActionListener(e -> {
                try {
                    int codigoJuego = Integer.parseInt(codigoJuegoField.getText());
                    int jugador1 = Integer.parseInt(jugador1Field.getText());
                    int jugador2 = Integer.parseInt(jugador2Field.getText());
                    String fechaStr = fechaField.getText();

                    // Intentamos convertir la fecha de String a java.sql.Date
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    java.util.Date parsedDate = dateFormat.parse(fechaStr);
                    Date sqlDate = new Date(parsedDate.getTime());

                    String resultado = resultadoField.getText();

                    agregarPartido(codigoJuego, jugador1, jugador2, sqlDate, resultado);
                } catch (NumberFormatException | ParseException nfe) {
                    JOptionPane.showMessageDialog(this, "Por favor, ingrese valores válidos.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            });
            panelBotones.add(agregarButton);

            // Botón para modificar partido
            JButton modificarButton = new JButton("Modificar");
            modificarButton.addActionListener(e -> {
                int selectedRow = partidosTable.getSelectedRow();
                if (selectedRow != -1) {
                    partidoSeleccionadoId = (int) partidosTableModel.getValueAt(selectedRow, 0);
                    try {
                        int codigoJuego = Integer.parseInt(codigoJuegoField.getText());
                        int jugador1 = Integer.parseInt(jugador1Field.getText());
                        int jugador2 = Integer.parseInt(jugador2Field.getText());
                        String fecha = fechaField.getText();
                        String resultado = resultadoField.getText();

                        modificarPartido(partidoSeleccionadoId, codigoJuego, jugador1, jugador2, fecha, resultado);
                    } catch (NumberFormatException nfe) {
                        JOptionPane.showMessageDialog(this, "Por favor, ingrese valores válidos.", "Error", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione un partido para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            });
            panelBotones.add(modificarButton);

            // Botón para eliminar partido
            JButton eliminarButton = new JButton("Eliminar");
            eliminarButton.addActionListener(e -> {
                int selectedRow = partidosTable.getSelectedRow();
                if (selectedRow != -1) {
                    int partidoId = (int) partidosTableModel.getValueAt(selectedRow, 0);
                    eliminarPartido(partidoId);
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione un partido para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            });
            panelBotones.add(eliminarButton);

            // Añadir panel de botones al panel de agregar
            gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
            panelAgregar.add(panelBotones, gbc);

            // Añadir el panel de agregar al panel principal
            add(panelAgregar, BorderLayout.SOUTH);

            // Establecer conexión a la base de datos
            ConexionBD.conectar();
            connection = ConexionBD.getConexion();
            if (connection != null) {
                cargarPartidos();
                cargarRankings();
            } else {
                JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // Método para cargar los partidos desde la base de datos a la tabla
        private void cargarPartidos() {
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT * FROM Matches")) {
                partidosTableModel.setRowCount(0); // Limpiar la tabla antes de cargar los datos
                while (resultSet.next()) {
                    int id = resultSet.getInt("match_id");
                    int codigoJuego = resultSet.getInt("game_code");
                    int jugador1 = resultSet.getInt("player_1_id");
                    int jugador2 = resultSet.getInt("player_2_id");
                    String fecha = resultSet.getString("match_date");
                    String resultado = resultSet.getString("result");

                    partidosTableModel.addRow(new Object[]{id, codigoJuego, jugador1, jugador2, fecha, resultado});
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al cargar partidos desde la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        // Método para cargar los rankings desde la base de datos a la tabla
        private void cargarRankings() {
            try (Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery(
                         "SELECT p.player_id, p.first_name || ' ' || p.last_name AS player_name, g.game_code, r.ranking " +
                                 "FROM players p " +
                                 "JOIN players_game_ranking r ON p.player_id = r.player_id " +
                                 "JOIN Games g ON g.game_code = r.game_code")) {
                rankingsTableModel.setRowCount(0); // Limpiar la tabla antes de cargar los datos
                while (resultSet.next()) {
                    int playerId = resultSet.getInt("player_id");
                    String playerName = resultSet.getString("player_name");
                    int gameCode = resultSet.getInt("game_code"); // Obtiene el código del juego
                    int ranking = resultSet.getInt("ranking");

                    rankingsTableModel.addRow(new Object[]{playerId, playerName, gameCode, ranking}); // Añade el Game Code aquí
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al cargar rankings desde la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        // Método para agregar un partido a la base de datos
        private void agregarPartido(int codigoJuego, int jugador1, int jugador2, Date fecha, String resultado) {
            String sql = "INSERT INTO Matches (game_code, player_1_id, player_2_id, match_date, result) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setInt(1, codigoJuego);
                statement.setInt(2, jugador1);
                statement.setInt(3, jugador2);
                statement.setDate(4, fecha);  // Usa setDate para el tipo Date
                statement.setString(5, resultado);
                statement.executeUpdate();
                JOptionPane.showMessageDialog(this, "Partido agregado exitosamente.");
                cargarPartidos(); // Refrescar la tabla después de la inserción
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al agregar el partido.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        // Método para modificar un partido en la base de datos
        private void modificarPartido(int partidoId, int codigoJuego, int jugador1, int jugador2, String fecha, String resultado) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "UPDATE Matches SET game_code = ?, player_1_id = ?, player_2_id = ?, match_date = ?, result = ? WHERE match_id = ?")) {

                // Convertir el String "fecha" a java.sql.Date
                Date matchDate = Date.valueOf(fecha); // El formato de fecha debe ser YYYY-MM-DD

                preparedStatement.setInt(1, codigoJuego);
                preparedStatement.setInt(2, jugador1);
                preparedStatement.setInt(3, jugador2);
                preparedStatement.setDate(4, matchDate); // Usar el objeto Date aquí
                preparedStatement.setString(5, resultado);
                preparedStatement.setInt(6, partidoId);

                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(this, "Partido modificado exitosamente.");
                cargarPartidos(); // Recargar la lista de partidos
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al modificar el partido.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use el formato YYYY-MM-DD.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }


        // Método para eliminar un partido de la base de datos
        private void eliminarPartido(int partidoId) {
            try (PreparedStatement preparedStatement = connection.prepareStatement(
                    "DELETE FROM Matches WHERE match_id = ?")) {
                preparedStatement.setInt(1, partidoId);
                preparedStatement.executeUpdate();
                JOptionPane.showMessageDialog(this, "Partido eliminado exitosamente.");
                cargarPartidos(); // Recargar la lista de partidos
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar el partido.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }


    // Clase para crear el panel de partidos entre equipos
    static class PanelPartidosEquipos extends JPanel {
        private JTable partidosTable;
        private DefaultTableModel tableModel;
        private Connection connection;
        private int partidoSeleccionadoId = -1; // ID del partido seleccionado para modificación

        public PanelPartidosEquipos() {
            setLayout(new BorderLayout());

            // Panel para la tabla de juegos
            JPanel panelTabla = new JPanel(new BorderLayout());
            panelTabla.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE),
                    "Lista de Partidos entre Equipos",
                    TitledBorder.LEFT,
                    TitledBorder.TOP,
                    new Font("Arial", Font.BOLD, 14),
                    Color.BLUE));
            panelTabla.setBackground(Color.LIGHT_GRAY);

            // Crear tabla para mostrar partidos
            tableModel = new DefaultTableModel();
            tableModel.addColumn("ID");
            tableModel.addColumn("Liga");
            tableModel.addColumn("Equipo 1");
            tableModel.addColumn("Equipo 2");
            tableModel.addColumn("Fecha");
            tableModel.addColumn("Código de Juego");
            tableModel.addColumn("Result");

            partidosTable = new JTable(tableModel);
            partidosTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); // Permitir selección de una sola fila
            JScrollPane scrollPane = new JScrollPane(partidosTable);
            panelTabla.add(scrollPane, BorderLayout.CENTER);
            add(panelTabla, BorderLayout.CENTER);

            // Panel para agregar/modificar partidos con estilo personalizado
            JPanel panelAgregar = new JPanel(new GridBagLayout());
            panelAgregar.setBorder(BorderFactory.createTitledBorder(
                    BorderFactory.createLineBorder(Color.BLUE),
                    "Agregar/Modificar Partido",
                    TitledBorder.LEFT,
                    TitledBorder.TOP,
                    new Font("Arial", Font.BOLD, 14),
                    Color.BLUE
            ));
            panelAgregar.setBackground(Color.LIGHT_GRAY);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Campos de entrada
            JTextField ligaField = new JTextField(15);
            JTextField equipo1Field = new JTextField(15);
            JTextField equipo2Field = new JTextField(15);
            JTextField fechaField = new JTextField(15);
            JTextField codigoJuegoField = new JTextField(15);

            // Agregar etiquetas y campos al panel
            gbc.gridx = 0; gbc.gridy = 0; panelAgregar.add(new JLabel("Liga:"), gbc);
            gbc.gridx = 1; gbc.gridy = 0; panelAgregar.add(ligaField, gbc);

            gbc.gridx = 0; gbc.gridy = 1; panelAgregar.add(new JLabel("Equipo 1:"), gbc);
            gbc.gridx = 1; gbc.gridy = 1; panelAgregar.add(equipo1Field, gbc);

            gbc.gridx = 0; gbc.gridy = 2; panelAgregar.add(new JLabel("Equipo 2:"), gbc);
            gbc.gridx = 1; gbc.gridy = 2; panelAgregar.add(equipo2Field, gbc);

            gbc.gridx = 0; gbc.gridy = 3; panelAgregar.add(new JLabel("Fecha (YYYY-MM-DD):"), gbc);
            gbc.gridx = 1; gbc.gridy = 3; panelAgregar.add(fechaField, gbc);

            gbc.gridx = 0; gbc.gridy = 4; panelAgregar.add(new JLabel("Código de Juego:"), gbc);
            gbc.gridx = 1; gbc.gridy = 4; panelAgregar.add(codigoJuegoField, gbc);

            // Panel para botones
            JPanel panelBotones = new JPanel(new FlowLayout());

            // Botón para agregar partido
            JButton agregarButton = new JButton("Agregar");
            agregarButton.addActionListener(e -> {
                String liga = ligaField.getText();
                String equipo1 = equipo1Field.getText();
                String equipo2 = equipo2Field.getText();
                String fecha = fechaField.getText();
                try {
                    int codigoJuego = Integer.parseInt(codigoJuegoField.getText());
                    agregarPartido(liga, equipo1, equipo2, fecha, codigoJuego);
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "El código de juego debe ser un número entero.", "Error de entrada", JOptionPane.ERROR_MESSAGE);
                }
            });
            panelBotones.add(agregarButton);

            // Botón para modificar partido
            JButton modificarButton = new JButton("Modificar");
            modificarButton.addActionListener(e -> {
                int selectedRow = partidosTable.getSelectedRow();
                if (selectedRow != -1) {
                    partidoSeleccionadoId = (int) tableModel.getValueAt(selectedRow, 0);
                    String liga = ligaField.getText();
                    String equipo1 = equipo1Field.getText();
                    String equipo2 = equipo2Field.getText();
                    String fecha = fechaField.getText();
                    try {
                        int codigoJuego = Integer.parseInt(codigoJuegoField.getText());
                        modificarPartido(partidoSeleccionadoId, liga, equipo1, equipo2, fecha, codigoJuego);
                    } catch (NumberFormatException ex) {
                        JOptionPane.showMessageDialog(this, "El código de juego debe ser un número entero.", "Error de entrada", JOptionPane.ERROR_MESSAGE);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione un partido para modificar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            });
            panelBotones.add(modificarButton);

            // Botón para eliminar partido
            JButton eliminarButton = new JButton("Eliminar");
            eliminarButton.addActionListener(e -> {
                int selectedRow = partidosTable.getSelectedRow();
                if (selectedRow != -1) {
                    int partidoId = (int) tableModel.getValueAt(selectedRow, 0);
                    int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar este partido?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
                    if (confirm == JOptionPane.YES_OPTION) {
                        eliminarPartido(partidoId);
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Seleccione un partido para eliminar.", "Advertencia", JOptionPane.WARNING_MESSAGE);
                }
            });
            panelBotones.add(eliminarButton);

            // Añadir panel de botones al panel de agregar
            gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 2;
            panelAgregar.add(panelBotones, gbc);

            // Añadir el panel de agregar al panel principal
            add(panelAgregar, BorderLayout.SOUTH);

            // Establecer conexión a la base de datos
            ConexionBD.conectar();
            connection = ConexionBD.getConexion();
            if (connection != null) {
                cargarPartidos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // Método para cargar los partidos desde la base de datos a la tabla
        private void cargarPartidos() {
            try {
                Statement statement = connection.createStatement();
                // Incluye el campo "result" en la consulta SQL
                ResultSet resultSet = statement.executeQuery("SELECT * FROM Matches_Teams");
                tableModel.setRowCount(0); // Limpiar la tabla antes de cargar los datos
                while (resultSet.next()) {
                    int id = resultSet.getInt("match_id");
                    String liga = obtenerNombreLiga(resultSet.getInt("league_id"));
                    String equipo1 = obtenerNombreEquipo(resultSet.getInt("team1_id"));
                    String equipo2 = obtenerNombreEquipo(resultSet.getInt("team2_id"));
                    String fecha = resultSet.getString("match_date");
                    int codigoJuego = resultSet.getInt("game_code");
                    String result = resultSet.getString("result");  // Obtener el resultado del partido

                    // Añadir el valor "result" a la fila
                    tableModel.addRow(new Object[]{id, liga, equipo1, equipo2, fecha, codigoJuego, result});
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al cargar partidos desde la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        private void agregarPartido(String liga, String equipo1, String equipo2, String fecha, int codigoJuego) {
            try {
                int idLiga = obtenerIdLiga(liga);
                int idEquipo1 = obtenerIdEquipo(equipo1);
                int idEquipo2 = obtenerIdEquipo(equipo2);

                // Convertir la cadena de texto 'fecha' a java.sql.Date
                java.sql.Date fechaSQL = java.sql.Date.valueOf(fecha); // La fecha debe estar en formato 'YYYY-MM-DD'

                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO Matches_Teams (league_id, team1_id, team2_id, match_date, game_code) VALUES (?, ?, ?, ?, ?)"
                );
                preparedStatement.setInt(1, idLiga);
                preparedStatement.setInt(2, idEquipo1);
                preparedStatement.setInt(3, idEquipo2);
                preparedStatement.setDate(4, fechaSQL); // Usar setDate para campos tipo 'date'
                preparedStatement.setInt(5, codigoJuego);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Partido agregado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarPartidos(); // Volver a cargar los partidos desde la base de datos
                } else {
                    JOptionPane.showMessageDialog(this, "Error al agregar el partido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al agregar el partido a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use YYYY-MM-DD.", "Error de formato", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void modificarPartido(int partidoId, String liga, String equipo1, String equipo2, String fecha, int codigoJuego) {
            try {
                int idLiga = obtenerIdLiga(liga);
                int idEquipo1 = obtenerIdEquipo(equipo1);
                int idEquipo2 = obtenerIdEquipo(equipo2);

                java.sql.Date fechaSQL = java.sql.Date.valueOf(fecha);

                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE Matches_Teams SET league_id=?, team1_id=?, team2_id=?, match_date=?, game_code=? WHERE match_id=?"
                );
                preparedStatement.setInt(1, idLiga);
                preparedStatement.setInt(2, idEquipo1);
                preparedStatement.setInt(3, idEquipo2);
                preparedStatement.setDate(4, fechaSQL);
                preparedStatement.setInt(5, codigoJuego);
                preparedStatement.setInt(6, partidoId);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Partido modificado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarPartidos(); // Volver a cargar los partidos desde la base de datos
                } else {
                    JOptionPane.showMessageDialog(this, "Error al modificar el partido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al modificar el partido en la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(this, "Formato de fecha inválido. Use YYYY-MM-DD.", "Error de formato", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void eliminarPartido(int partidoId) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "DELETE FROM Matches_Teams WHERE match_id=?"
                );
                preparedStatement.setInt(1, partidoId);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Partido eliminado exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    cargarPartidos(); // Volver a cargar los partidos desde la base de datos
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar el partido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar el partido de la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        // Método para obtener el nombre de la liga a partir de su ID
        private String obtenerNombreLiga(int idLiga) {
            String nombreLiga = "";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT league_name FROM Leagues WHERE league_id = ?");
                preparedStatement.setInt(1, idLiga);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    nombreLiga = resultSet.getString("league_name");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return nombreLiga;
        }

        // Método para obtener el nombre de un equipo a partir de su ID
        private String obtenerNombreEquipo(int idEquipo) {
            String nombreEquipo = "";
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT team_name FROM Teams WHERE team_id = ?");
                preparedStatement.setInt(1, idEquipo);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    nombreEquipo = resultSet.getString("team_name");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return nombreEquipo;
        }

        // Método para obtener el ID de una liga a partir de su nombre
        private int obtenerIdLiga(String nombreLiga) {
            int idLiga = -1;
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT league_id FROM Leagues WHERE league_name = ?");
                preparedStatement.setString(1, nombreLiga);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    idLiga = resultSet.getInt("league_id");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return idLiga;
        }

        // Método para obtener el ID de un equipo a partir de su nombre
        private int obtenerIdEquipo(String nombreEquipo) {
            int idEquipo = -1;
            try {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT team_id FROM Teams WHERE team_name = ?");
                preparedStatement.setString(1, nombreEquipo);
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    idEquipo = resultSet.getInt("team_id");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return idEquipo;
        }
    }


    // Clase para crear el panel de relación entre Jugadores y Equipos
    static class PanelJE extends JPanel {
        private JTable JETable;
        private DefaultTableModel tableModel;
        private Connection connection;
        private int selectedTeamID = -1;
        private int selectedPlayerID = -1;

        public PanelJE() {
            setLayout(new BorderLayout());

            // Panel para la tabla de juegos
            JPanel panelTabla = new JPanel(new BorderLayout());
            panelTabla.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE),
                    "Relación entre Jugadores y Equipos",
                    TitledBorder.LEFT,
                    TitledBorder.TOP,
                    new Font("Arial", Font.BOLD, 14),
                    Color.BLUE));
            panelTabla.setBackground(Color.LIGHT_GRAY);


            // Crear tabla para mostrar relación entre Jugadores y Equipos
            tableModel = new DefaultTableModel();
            tableModel.addColumn("ID_Equipo");
            tableModel.addColumn("ID_Jugador");
            tableModel.addColumn("Fecha_Desde");
            tableModel.addColumn("Fecha_Hasta");

            JETable = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(JETable);
            add(scrollPane, BorderLayout.CENTER);
            panelTabla.add(scrollPane, BorderLayout.CENTER);

            add(panelTabla, BorderLayout.CENTER);

            // Panel para asignar jugadores a equipos
            JPanel panelAsignar = new JPanel();
            panelAsignar.setLayout(new GridBagLayout());
            panelAsignar.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE),
                    "Asignar Jugador a Equipo",
                    TitledBorder.LEFT,
                    TitledBorder.TOP,
                    new Font("Arial", Font.BOLD, 14),
                    Color.BLUE));
            panelAsignar.setBackground(Color.LIGHT_GRAY);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5); // Espaciado entre componentes
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Campos de entrada
            JTextField ID_JugadorField = new JTextField(15);
            JTextField ID_EquipoField = new JTextField(15);
            JTextField Fecha_AsignacionField = new JTextField(15);
            JTextField Fecha_HastaField = new JTextField(15); // Nuevo campo para Fecha Hasta

            // Agregar etiquetas y campos al panel
            gbc.gridx = 0; gbc.gridy = 0; panelAsignar.add(new JLabel("ID_Equipo:"), gbc);
            gbc.gridx = 1; gbc.gridy = 0; panelAsignar.add(ID_EquipoField, gbc);
            gbc.gridx = 0; gbc.gridy = 1; panelAsignar.add(new JLabel("ID_Jugador:"), gbc);
            gbc.gridx = 1; gbc.gridy = 1; panelAsignar.add(ID_JugadorField, gbc);
            gbc.gridx = 0; gbc.gridy = 2; panelAsignar.add(new JLabel("Fecha_Desde:"), gbc);
            gbc.gridx = 1; gbc.gridy = 2; panelAsignar.add(Fecha_AsignacionField, gbc);
            gbc.gridx = 0; gbc.gridy = 3; panelAsignar.add(new JLabel("Fecha_Hasta:"), gbc);
            gbc.gridx = 1; gbc.gridy = 3; panelAsignar.add(Fecha_HastaField, gbc);

            // Panel para botones
            JPanel panelBotones = new JPanel();
            panelBotones.setLayout(new FlowLayout());

            // Botón para asignar jugador
            JButton asignarButton = new JButton("Asignar");
            asignarButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String teamID = ID_EquipoField.getText();
                    String playerID = ID_JugadorField.getText();
                    String dateFrom = Fecha_AsignacionField.getText();

                    agregarJugador(teamID, playerID, dateFrom);
                }
            });
            panelBotones.add(asignarButton);

            // Botón para modificar relación
            JButton modificarButton = new JButton("Guardar");
            modificarButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (selectedTeamID >= 0 && selectedPlayerID >= 0) {
                        String dateFrom = Fecha_AsignacionField.getText();
                        String dateTo = Fecha_HastaField.getText(); // Obtener Fecha Hasta

                        modificarRelacion(selectedTeamID, selectedPlayerID, dateFrom, dateTo);
                    } else {
                        JOptionPane.showMessageDialog(null, "Seleccione una relación para modificar.");
                    }
                }
            });
            panelBotones.add(modificarButton);

            // Botón para eliminar relación
            JButton eliminarButton = new JButton("Eliminar");
            eliminarButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (selectedTeamID >= 0 && selectedPlayerID >= 0) {
                        int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar esta relación?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            eliminarRelacion(selectedTeamID, selectedPlayerID);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Seleccione una relación para eliminar.");
                    }
                }
            });
            panelBotones.add(eliminarButton);

            // Agregar panel de botones al panel de asignación
            gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 2; panelAsignar.add(panelBotones, gbc);

            add(panelAsignar, BorderLayout.SOUTH);

            // Establecer conexión a la base de datos
            ConexionBD.conectar();
            connection = ConexionBD.getConexion();
            if (connection != null) {
                cargarJugadoresEquipos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            // Listener para seleccionar fila de la tabla
            JETable.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    int selectedRow = JETable.getSelectedRow();
                    if (selectedRow >= 0) {
                        selectedTeamID = (int) tableModel.getValueAt(selectedRow, 0);
                        selectedPlayerID = (int) tableModel.getValueAt(selectedRow, 1);
                        Fecha_AsignacionField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                        Fecha_HastaField.setText(tableModel.getValueAt(selectedRow, 3) != null ? tableModel.getValueAt(selectedRow, 3).toString() : ""); // Manejar nulos
                    }
                }
            });
        }

        // Método para cargar la relación entre Jugadores y Equipos desde la base de datos a la tabla
        private void cargarJugadoresEquipos() {
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM Team_Players");
                while (resultSet.next()) {
                    int teamID = resultSet.getInt("team_id");
                    int playerID = resultSet.getInt("player_id");
                    Date dateFrom = resultSet.getDate("date_from");
                    Date dateTo = resultSet.getDate("date_to");

                    tableModel.addRow(new Object[]{teamID, playerID, dateFrom, dateTo});
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al cargar jugadores y equipos desde la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        // Método para asignar un jugador a un equipo en la base de datos y actualizar la tabla
        private void agregarJugador(String teamID, String playerID, String dateFrom) {
            try {
                int teamId = Integer.parseInt(teamID);
                int playerId = Integer.parseInt(playerID);
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                java.util.Date parsedDateFrom = sdf.parse(dateFrom);
                java.sql.Date assignedDateFrom = new java.sql.Date(parsedDateFrom.getTime());

                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO Team_Players (team_id, player_id, date_from, date_to) VALUES (?, ?, ?, null)"
                );
                preparedStatement.setInt(1, teamId);
                preparedStatement.setInt(2, playerId);
                preparedStatement.setDate(3, assignedDateFrom);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Jugador asignado a un equipo exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    tableModel.setRowCount(0); // Limpiar la tabla antes de cargar los datos actualizados
                    cargarJugadoresEquipos(); // Volver a cargar los datos desde la base de datos
                } else {
                    JOptionPane.showMessageDialog(this, "Error al asignar el jugador.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Formato de fecha incorrecto. Utilice yyyy-MM-dd.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al asignar el jugador.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }

        // Método para modificar una relación entre un jugador y un equipo en la base de datos
        private void modificarRelacion(int teamId, int playerId, String dateFrom, String dateTo) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                java.sql.Date assignedDateFrom = new java.sql.Date(sdf.parse(dateFrom).getTime());
                java.sql.Date assignedDateTo = dateTo.isEmpty() ? null : new java.sql.Date(sdf.parse(dateTo).getTime());

                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE Team_Players SET date_from = ?, date_to = ? WHERE team_id = ? AND player_id = ?"
                );
                preparedStatement.setDate(1, assignedDateFrom);
                preparedStatement.setDate(2, assignedDateTo);
                preparedStatement.setInt(3, teamId);
                preparedStatement.setInt(4, playerId);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Relación modificada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    tableModel.setRowCount(0); // Limpiar la tabla antes de cargar los datos actualizados
                    cargarJugadoresEquipos(); // Volver a cargar los datos desde la base de datos
                } else {
                    JOptionPane.showMessageDialog(this, "Error al modificar la relación.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException | ParseException e) {
                JOptionPane.showMessageDialog(this, "Error al modificar la relación.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        // Método para eliminar una relación entre un jugador y un equipo en la base de datos
        private void eliminarRelacion(int teamId, int playerId) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "DELETE FROM Team_Players WHERE team_id = ? AND player_id = ?"
                );
                preparedStatement.setInt(1, teamId);
                preparedStatement.setInt(2, playerId);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Relación eliminada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    tableModel.setRowCount(0); // Limpiar la tabla antes de cargar los datos actualizados
                    cargarJugadoresEquipos(); // Volver a cargar los datos desde la base de datos
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar la relación.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar la relación.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }


    // Clase para crear el panel de relación entre Ligas y Equipos
    static class PanelLE extends JPanel {
        private JTable LETable;
        private DefaultTableModel tableModel;
        private Connection connection;
        private int selectedLeagueID = -1;
        private int selectedTeamID = -1;

        public PanelLE() {
            setLayout(new BorderLayout());

            // Panel para la tabla de juegos
            JPanel panelTabla = new JPanel(new BorderLayout());
            panelTabla.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE),
                    "Relación entre Ligas y Equipos",
                    TitledBorder.LEFT,
                    TitledBorder.TOP,
                    new Font("Arial", Font.BOLD, 14),
                    Color.BLUE));
            panelTabla.setBackground(Color.LIGHT_GRAY);


            // Crear tabla para mostrar relación entre Ligas y Equipos
            tableModel = new DefaultTableModel();
            tableModel.addColumn("ID_Liga");
            tableModel.addColumn("ID_Equipo");

            LETable = new JTable(tableModel);
            JScrollPane scrollPane = new JScrollPane(LETable);
            add(scrollPane, BorderLayout.CENTER);
            panelTabla.add(scrollPane, BorderLayout.CENTER);

            add(panelTabla, BorderLayout.CENTER);

            // Panel para establecer relación entre ligas y equipos
            JPanel panelRelacion = new JPanel();
            panelRelacion.setLayout(new GridBagLayout());
            panelRelacion.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLUE),
                    "Establecer Relación entre Liga y Equipo",
                    TitledBorder.LEFT,
                    TitledBorder.TOP,
                    new Font("Arial", Font.BOLD, 14),
                    Color.BLUE));
            panelRelacion.setBackground(Color.LIGHT_GRAY);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(5, 5, 5, 5); // Espaciado entre componentes
            gbc.fill = GridBagConstraints.HORIZONTAL;

            // Campos de entrada
            JTextField ID_LigaField = new JTextField(15);
            JTextField ID_EquipoField = new JTextField(15);

            // Agregar etiquetas y campos al panel
            gbc.gridx = 0; gbc.gridy = 0; panelRelacion.add(new JLabel("ID_Liga:"), gbc);
            gbc.gridx = 1; gbc.gridy = 0; panelRelacion.add(ID_LigaField, gbc);
            gbc.gridx = 0; gbc.gridy = 1; panelRelacion.add(new JLabel("ID_Equipo:"), gbc);
            gbc.gridx = 1; gbc.gridy = 1; panelRelacion.add(ID_EquipoField, gbc);

            // Panel para botones
            JPanel panelBotones = new JPanel();
            panelBotones.setLayout(new FlowLayout());

            // Botón para establecer relación
            JButton relacionButton = new JButton("Establecer Relación");
            relacionButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    String ID_Liga = ID_LigaField.getText();
                    String ID_Equipo = ID_EquipoField.getText();
                    establecerRelacionLigaEquipo(ID_Liga, ID_Equipo);
                }
            });
            panelBotones.add(relacionButton);

            // Botón para modificar relación
            JButton modificarButton = new JButton("Modificar");
            modificarButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (selectedLeagueID >= 0 && selectedTeamID >= 0) {
                        String leagueID = ID_LigaField.getText();
                        String teamID = ID_EquipoField.getText();
                        modificarRelacion(leagueID, teamID);
                    } else {
                        JOptionPane.showMessageDialog(null, "Seleccione una relación para modificar.");
                    }
                }
            });
            panelBotones.add(modificarButton);

            // Botón para eliminar relación
            JButton eliminarButton = new JButton("Eliminar");
            eliminarButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (selectedLeagueID >= 0 && selectedTeamID >= 0) {
                        int confirm = JOptionPane.showConfirmDialog(null, "¿Está seguro de que desea eliminar esta relación?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
                        if (confirm == JOptionPane.YES_OPTION) {
                            eliminarRelacion(selectedLeagueID, selectedTeamID);
                        }
                    } else {
                        JOptionPane.showMessageDialog(null, "Seleccione una relación para eliminar.");
                    }
                }
            });
            panelBotones.add(eliminarButton);

            // Agregar panel de botones al panel de relación
            gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 2; panelRelacion.add(panelBotones, gbc);

            add(panelRelacion, BorderLayout.SOUTH);

            // Establecer conexión a la base de datos
            ConexionBD.conectar();
            connection = ConexionBD.getConexion();
            if (connection != null) {
                cargarLigasEquipos();
            } else {
                JOptionPane.showMessageDialog(this, "Error al conectar a la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
            }

            // Listener para seleccionar fila de la tabla
            LETable.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    int selectedRow = LETable.getSelectedRow();
                    if (selectedRow >= 0) {
                        selectedLeagueID = (int) tableModel.getValueAt(selectedRow, 0);
                        selectedTeamID = (int) tableModel.getValueAt(selectedRow, 1);
                        ID_LigaField.setText(String.valueOf(selectedLeagueID));
                        ID_EquipoField.setText(String.valueOf(selectedTeamID));
                    }
                }
            });
        }

        // Método para cargar la relación entre Ligas y Equipos desde la base de datos a la tabla
        private void cargarLigasEquipos() {
            try {
                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery("SELECT * FROM Leagues_Teams");
                while (resultSet.next()) {
                    int leagueID = resultSet.getInt("league_id");
                    int teamID = resultSet.getInt("team_id");

                    tableModel.addRow(new Object[]{leagueID, teamID});
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al cargar la relación entre ligas y equipos desde la base de datos.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        // Método para establecer la relación entre una liga y un equipo en la base de datos y actualizar la tabla
        private void establecerRelacionLigaEquipo(String ID_Liga, String ID_Equipo) {
            try {
                int leagueID = Integer.parseInt(ID_Liga);
                int teamID = Integer.parseInt(ID_Equipo);

                PreparedStatement preparedStatement = connection.prepareStatement(
                        "INSERT INTO Leagues_Teams (league_id, team_id) VALUES (?, ?)"
                );
                preparedStatement.setInt(1, leagueID);
                preparedStatement.setInt(2, teamID);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Relación establecida exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    tableModel.setRowCount(0); // Limpiar la tabla antes de cargar los datos actualizados
                    cargarLigasEquipos(); // Volver a cargar los datos desde la base de datos
                } else {
                    JOptionPane.showMessageDialog(this, "Error al establecer la relación.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID de liga y equipo deben ser números enteros.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al establecer la relación.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        // Método para modificar una relación entre una liga y un equipo en la base de datos
        private void modificarRelacion(String leagueID, String teamID) {
            try {
                int leagueId = Integer.parseInt(leagueID);
                int teamId = Integer.parseInt(teamID);

                PreparedStatement preparedStatement = connection.prepareStatement(
                        "UPDATE Leagues_Teams SET league_id = ?, team_id = ? WHERE league_id = ? AND team_id = ?"
                );
                preparedStatement.setInt(1, leagueId);
                preparedStatement.setInt(2, teamId);
                preparedStatement.setInt(3, selectedLeagueID);
                preparedStatement.setInt(4, selectedTeamID);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Relación modificada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    tableModel.setRowCount(0); // Limpiar la tabla antes de cargar los datos actualizados
                    cargarLigasEquipos(); // Volver a cargar los datos desde la base de datos
                } else {
                    JOptionPane.showMessageDialog(this, "Error al modificar la relación.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "ID de liga y equipo deben ser números enteros.", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al modificar la relación.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }

        // Método para eliminar una relación entre una liga y un equipo en la base de datos
        private void eliminarRelacion(int leagueId, int teamId) {
            try {
                PreparedStatement preparedStatement = connection.prepareStatement(
                        "DELETE FROM Leagues_Teams WHERE league_id = ? AND team_id = ?"
                );
                preparedStatement.setInt(1, leagueId);
                preparedStatement.setInt(2, teamId);

                int rowsAffected = preparedStatement.executeUpdate();
                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "Relación eliminada exitosamente.", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                    tableModel.setRowCount(0); // Limpiar la tabla antes de cargar los datos actualizados
                    cargarLigasEquipos(); // Volver a cargar los datos desde la base de datos
                } else {
                    JOptionPane.showMessageDialog(this, "Error al eliminar la relación.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException e) {
                JOptionPane.showMessageDialog(this, "Error al eliminar la relación.", "Error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        }
    }


    // Clase para crear el panel de procesos de negocio
    static class PanelProcesos extends JPanel {
        private static Connection conn;
        private static Statement stmt;
        private static ResultSet rs;
        private CardLayout cardLayout;
        private JPanel contentPanel;
        private JMenu panelContenedor;


        public PanelProcesos() {
            cardLayout = new CardLayout();
            contentPanel = new JPanel(cardLayout);
            setLayout(new BorderLayout());

            // Menú superior para las diferentes acciones
            JMenuBar menuBar = new JMenuBar();
            JMenu menuProcesos = new JMenu("Procesos");

            // Item para asignar jugadores a equipos
            JMenuItem asignarJugadores = new JMenuItem("Asignar jugadores a Equipos");
            asignarJugadores.addActionListener(e -> mostrarPanel("asignarJugadores"));
            menuProcesos.add(asignarJugadores);

            // Item para organizar partidos entre jugadores
            JMenuItem organizarPartidosJugadores = new JMenuItem("Organizar partidos entre Jugadores");
            organizarPartidosJugadores.addActionListener(e -> mostrarPanel("organizarPartidosJugadores"));
            menuProcesos.add(organizarPartidosJugadores);

            // Item para organizar partidos entre ligas
            JMenuItem organizarPartidosLigas = new JMenuItem("Organizar partidos entre Ligas");
            organizarPartidosLigas.addActionListener(e -> mostrarPanel("organizarPartidosLigas"));
            menuProcesos.add(organizarPartidosLigas);

            // Item para organizar partidos entre equipos de una liga
            JMenuItem organizarPartidosEquipos = new JMenuItem("Organizar partidos entre Equipos de una Liga");
            organizarPartidosEquipos.addActionListener(e -> mostrarPanel("organizarPartidosEquipos"));
            menuProcesos.add(organizarPartidosEquipos);

            // Agregar el menú a la barra de menú
            menuBar.add(menuProcesos);
            add(menuBar, BorderLayout.NORTH);


            // Panel para Asignar jugadores a equipos
            JPanel asignarJugadoresPanel = new JPanel(new GridBagLayout());
            asignarJugadoresPanel.setBorder(BorderFactory.createTitledBorder("Asignar Jugadores a Equipos"));
            asignarJugadoresPanel.setBackground(new Color(84, 134, 136));

            //
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(10, 10, 10, 10);
            gbc.fill = GridBagConstraints.HORIZONTAL;

            JLabel equiposLabel = new JLabel("Seleccionar Equipo:");
            gbc.gridx = 0;
            gbc.gridy = 0;
            asignarJugadoresPanel.add(equiposLabel, gbc);

            JComboBox<String> equiposComboBox = new JComboBox<>();
            gbc.gridx = 1;
            gbc.gridy = 0;
            asignarJugadoresPanel.add(equiposComboBox, gbc);

            JLabel jugadoresLabel = new JLabel("Seleccionar Jugador:");
            gbc.gridx = 0;
            gbc.gridy = 1;
            asignarJugadoresPanel.add(jugadoresLabel, gbc);

            JComboBox<String> jugadoresComboBox = new JComboBox<>();
            gbc.gridx = 1;
            gbc.gridy = 1;
            asignarJugadoresPanel.add(jugadoresComboBox, gbc);

            JButton asignarButton = new JButton("Asignar");
            gbc.gridx = 0;
            gbc.gridy = 2;
            gbc.gridwidth = 2;
            asignarJugadoresPanel.add(asignarButton, gbc);

            // Llamar a los métodos para cargar datos en los ComboBox
            cargarEquiposEnComboBox(equiposComboBox);
            cargarJugadoresEnComboBox(jugadoresComboBox);

            asignarButton.addActionListener(e -> {
                String equipoSeleccionado = (String) equiposComboBox.getSelectedItem();
                String jugadorSeleccionado = (String) jugadoresComboBox.getSelectedItem();
                asignarJugadorAEquipo(equipoSeleccionado, jugadorSeleccionado);
            });


            /*
            Separacion
             */


            // Panel para organizar partidos entre jugadores
            JPanel organizarPartidosJugadoresPanel = new JPanel(new GridBagLayout());
            organizarPartidosJugadoresPanel.setBorder(BorderFactory.createTitledBorder("Organizar Partido entre Jugadores"));
            organizarPartidosJugadoresPanel.setBackground(new Color(84, 134, 136)); // Color de fondo

            GridBagConstraints gbc1 = new GridBagConstraints();
            gbc1.insets = new Insets(10, 10, 10, 10);
            gbc1.fill = GridBagConstraints.HORIZONTAL;

            JLabel lblCodigoJuego = new JLabel("Código de Juego:");
            gbc1.gridx = 0;
            gbc1.gridy = 0;
            organizarPartidosJugadoresPanel.add(lblCodigoJuego, gbc1);

            JTextField txtCodigoJuego = new JTextField();
            txtCodigoJuego.setPreferredSize(new Dimension(200, 25));
            gbc1.gridx = 1;
            gbc1.gridy = 0;
            organizarPartidosJugadoresPanel.add(txtCodigoJuego, gbc1);

            JLabel lblJugador1 = new JLabel("Seleccionar Jugador 1:");
            gbc1.gridx = 0;
            gbc1.gridy = 1;
            organizarPartidosJugadoresPanel.add(lblJugador1, gbc1);

            JComboBox<String> cmbJugador1 = new JComboBox<>();
            gbc1.gridx = 1;
            gbc1.gridy = 1;
            organizarPartidosJugadoresPanel.add(cmbJugador1, gbc1);

            JLabel lblJugador2 = new JLabel("Seleccionar Jugador 2:");
            gbc1.gridx = 0;
            gbc1.gridy = 2;
            organizarPartidosJugadoresPanel.add(lblJugador2, gbc1);

            JComboBox<String> cmbJugador2 = new JComboBox<>();
            gbc1.gridx = 1;
            gbc1.gridy = 2;
            organizarPartidosJugadoresPanel.add(cmbJugador2, gbc1);

            JButton btnOrganizarPartido = new JButton("Organizar Partido");
            gbc1.gridx = 0;
            gbc1.gridy = 3;
            gbc1.gridwidth = 2;
            organizarPartidosJugadoresPanel.add(btnOrganizarPartido, gbc1);

// Llamar a los métodos para cargar datos en los ComboBox
            cargarJugadoresEnComboBox(cmbJugador1);
            cargarJugadoresEnComboBox(cmbJugador2);

            btnOrganizarPartido.addActionListener(e -> {
                String jugador1Seleccionado = (String) cmbJugador1.getSelectedItem();
                String jugador2Seleccionado = (String) cmbJugador2.getSelectedItem();
                String codigoJuego = txtCodigoJuego.getText();

                organizarPartidoEntreJugadores(jugador1Seleccionado, jugador2Seleccionado, codigoJuego);
            });
            /*

             */
            // Panel para organizar partidos entre ligas
            JPanel organizarPartidosLigasPanel = new JPanel(new GridBagLayout());
            organizarPartidosLigasPanel.setBorder(BorderFactory.createTitledBorder("Organizar Partidos entre Ligas"));
            organizarPartidosLigasPanel.setBackground(new Color(35, 87, 132)); // Color de fondo

            GridBagConstraints gbc2 = new GridBagConstraints();
            gbc2.insets = new Insets(10, 10, 10, 10);
            gbc2.fill = GridBagConstraints.HORIZONTAL;

            JLabel lblLiga1 = new JLabel("Liga 1:");
            gbc2.gridx = 0;
            gbc2.gridy = 0;
            organizarPartidosLigasPanel.add(lblLiga1, gbc2);

            JComboBox<String> cmbLigas1 = new JComboBox<>();
            gbc2.gridx = 1;
            gbc2.gridy = 0;
            organizarPartidosLigasPanel.add(cmbLigas1, gbc2);

            JLabel lblEquipo1 = new JLabel("Equipo de Liga 1:");
            gbc2.gridx = 0;
            gbc2.gridy = 1;
            organizarPartidosLigasPanel.add(lblEquipo1, gbc2);

            JComboBox<String> cmbEquiposLiga1 = new JComboBox<>();
            gbc2.gridx = 1;
            gbc2.gridy = 1;
            organizarPartidosLigasPanel.add(cmbEquiposLiga1, gbc2);

            JLabel lblLiga2 = new JLabel("Liga 2:");
            gbc2.gridx = 0;
            gbc2.gridy = 2;
            organizarPartidosLigasPanel.add(lblLiga2, gbc2);

            JComboBox<String> cmbLigas2 = new JComboBox<>();
            gbc2.gridx = 1;
            gbc2.gridy = 2;
            organizarPartidosLigasPanel.add(cmbLigas2, gbc2);

            JLabel lblEquipo2 = new JLabel("Equipo de Liga 2:");
            gbc2.gridx = 0;
            gbc2.gridy = 3;
            organizarPartidosLigasPanel.add(lblEquipo2, gbc2);

            JComboBox<String> cmbEquiposLiga2 = new JComboBox<>();
            gbc2.gridx = 1;
            gbc2.gridy = 3;
            organizarPartidosLigasPanel.add(cmbEquiposLiga2, gbc2);

            JLabel CodigoJuego = new JLabel("Código del Juego:");
            gbc2.gridx = 0;
            gbc2.gridy = 4;
            organizarPartidosLigasPanel.add(CodigoJuego, gbc2);

            JTextField tXtCodigoJuego = new JTextField();
            tXtCodigoJuego.setPreferredSize(new Dimension(200, 25));
            gbc2.gridx = 1;
            gbc2.gridy = 4;
            organizarPartidosLigasPanel.add(tXtCodigoJuego, gbc2);

            JButton btn_OrganizarPartido = new JButton("Organizar Partido");
            gbc2.gridx = 0;
            gbc2.gridy = 5;
            gbc2.gridwidth = 2;
            organizarPartidosLigasPanel.add(btn_OrganizarPartido, gbc2);

            // Llamar a los métodos para cargar datos en los ComboBox
            cargarLigasEnComboBox(cmbLigas1);
            cargarLigasEnComboBox(cmbLigas2);

            cmbLigas1.addActionListener(e -> {
                String ligaSeleccionada = (String) cmbLigas1.getSelectedItem();
                cargarEquiposDeLigaEnComboBox(ligaSeleccionada, cmbEquiposLiga1);
            });

            cmbLigas2.addActionListener(e -> {
                String ligaSeleccionada = (String) cmbLigas2.getSelectedItem();
                cargarEquiposDeLigaEnComboBox(ligaSeleccionada, cmbEquiposLiga2);
            });


            btn_OrganizarPartido.addActionListener(e -> {
                String liga1Seleccionada = (String) cmbLigas1.getSelectedItem();
                String equipo1Seleccionado = (String) cmbEquiposLiga1.getSelectedItem();
                String liga2Seleccionada = (String) cmbLigas2.getSelectedItem();
                String equipo2Seleccionado = (String) cmbEquiposLiga2.getSelectedItem();
                String codigoJuego = tXtCodigoJuego.getText();

                panelOrganizarPartidoLigas(liga1Seleccionada, equipo1Seleccionado, liga2Seleccionada, equipo2Seleccionado, codigoJuego);
            });

            /*

             */

            // Panel para organizar partidos entre equipos de una liga
            JPanel organizarPartidosEquiposPanel = new JPanel(new GridBagLayout());
            organizarPartidosEquiposPanel.setBorder(BorderFactory.createTitledBorder("Organizar Partidos entre Equipos de una Liga"));
            organizarPartidosEquiposPanel.setBackground(new Color(35, 87, 132)); // Color de fondo

            GridBagConstraints gbc3 = new GridBagConstraints();
            gbc3.insets = new Insets(10, 10, 10, 10);
            gbc3.fill = GridBagConstraints.HORIZONTAL;

            JLabel lblLiga = new JLabel("Liga:");
            gbc3.gridx = 0;
            gbc3.gridy = 0;
            organizarPartidosEquiposPanel.add(lblLiga, gbc3);

            JComboBox<String> cmbLiga = new JComboBox<>();
            gbc3.gridx = 1;
            gbc3.gridy = 0;
            organizarPartidosEquiposPanel.add(cmbLiga, gbc3);

            JLabel lbl_Equipo1 = new JLabel("Equipo 1:");
            gbc3.gridx = 0;
            gbc3.gridy = 1;
            organizarPartidosEquiposPanel.add(lbl_Equipo1, gbc3);

            JComboBox<String> cmbEquipo1 = new JComboBox<>();
            gbc3.gridx = 1;
            gbc3.gridy = 1;
            organizarPartidosEquiposPanel.add(cmbEquipo1, gbc3);

            JLabel lbl_Equipo2 = new JLabel("Equipo 2:");
            gbc3.gridx = 0;
            gbc3.gridy = 2;
            organizarPartidosEquiposPanel.add(lbl_Equipo2, gbc3);

            JComboBox<String> cmbEquipo2 = new JComboBox<>();
            gbc3.gridx = 1;
            gbc3.gridy = 2;
            organizarPartidosEquiposPanel.add(cmbEquipo2, gbc3);

            JLabel lbl_CodigoJuego = new JLabel("Código del Juego:");
            gbc3.gridx = 0;
            gbc3.gridy = 3;
            organizarPartidosEquiposPanel.add(lbl_CodigoJuego, gbc3);

            JTextField txtCodigo_Juego = new JTextField();
            txtCodigo_Juego.setPreferredSize(new Dimension(200, 25));
            gbc3.gridx = 1;
            gbc3.gridy = 3;
            organizarPartidosEquiposPanel.add(txtCodigo_Juego, gbc3);

            JButton btnOrganizar_Partido = new JButton("Organizar Partido");
            gbc3.gridx = 0;
            gbc3.gridy = 4;
            gbc3.gridwidth = 2;
            organizarPartidosEquiposPanel.add(btnOrganizar_Partido, gbc3);

            // Llamar a los métodos para cargar datos en los ComboBox
            cargarLigasEnComboBox(cmbLiga);
            cargarEquiposEnComboBox(cmbEquipo1);
            cargarEquiposEnComboBox(cmbEquipo2);

            btnOrganizar_Partido.addActionListener(e -> {
                String ligaSeleccionada = (String) cmbLiga.getSelectedItem();
                String equipo1Seleccionado = (String) cmbEquipo1.getSelectedItem();
                String equipo2Seleccionado = (String) cmbEquipo2.getSelectedItem();
                String codigoJuego = txtCodigo_Juego.getText();

                organizarPartidoEntreEquiposDeLiga(ligaSeleccionada, equipo1Seleccionado, equipo2Seleccionado, codigoJuego);
            });

            // Agregar los paneles al panel principal con el CardLayout
            contentPanel.add(asignarJugadoresPanel, "asignarJugadores");
            contentPanel.add(organizarPartidosJugadoresPanel, "organizarPartidosJugadores");
            contentPanel.add(organizarPartidosLigasPanel, "organizarPartidosLigas");
            contentPanel.add(organizarPartidosEquiposPanel, "organizarPartidosEquipos");

            // Agregar el panel principal al layout de la ventana
            add(contentPanel, BorderLayout.CENTER);

            // Mostrar el primer panel por defecto
            cardLayout.show(contentPanel, "asignarJugadores");
        }

        private void mostrarPanel (String panelName){
            System.out.println("Mostrando panel: " + panelName);
            cardLayout.show(contentPanel,panelName);
            contentPanel.revalidate();
            contentPanel.repaint();
        }

        // Método para crear el panel de organizar partidos entre equipos de una liga
        private JPanel crearPanelOrganizarPartidoLigaUnica() {
            JPanel panelOrganizarPartidoLigaUnica = new JPanel(new BorderLayout());
            panelOrganizarPartidoLigaUnica.setBorder(BorderFactory.createTitledBorder("Organizar Partido entre Equipos de una Liga"));

            // ComboBox y ActionListener para seleccionar la liga
            JComboBox<String> ligaUnicaComboBox = new JComboBox<>();
            JComboBox<String> equiposComboBoxLigaUnica1 = new JComboBox<>();
            JComboBox<String> equiposComboBoxLigaUnica2 = new JComboBox<>();

            // Cargar las ligas en el ComboBox
            cargarLigasEnComboBox(ligaUnicaComboBox);

            // Cargar los equipos en los ComboBox
            cargarEquiposEnComboBox(equiposComboBoxLigaUnica1);
            cargarEquiposEnComboBox(equiposComboBoxLigaUnica2);

            // Campo de texto para ingresar el código del juego
            JTextField codigoJuegoTextFieldLigaUnica = new JTextField();

            // Botón para organizar el partido
            JButton organizarPartidoButtonLigaUnica = new JButton("Organizar Partido");
            organizarPartidoButtonLigaUnica.addActionListener(e -> {
                String ligaSeleccionada = (String) ligaUnicaComboBox.getSelectedItem();
                String equipo1Seleccionado = (String) equiposComboBoxLigaUnica1.getSelectedItem();
                String equipo2Seleccionado = (String) equiposComboBoxLigaUnica2.getSelectedItem();

                String codigoJuego = codigoJuegoTextFieldLigaUnica.getText();

                organizarPartidoEntreEquiposDeLiga(ligaSeleccionada, equipo1Seleccionado, equipo2Seleccionado, codigoJuego);
            });

            // Panel para organizar los componentes
            JPanel formPanel = new JPanel(new BorderLayout());
            JPanel equiposPanel = new JPanel(new GridLayout(3, 1));
            equiposPanel.add(new JLabel("Liga:"));
            equiposPanel.add(ligaUnicaComboBox);
            equiposPanel.add(new JLabel("Equipos:"));

            // Panel para los ComboBox de equipos
            JPanel equiposComboBoxPanel = new JPanel(new GridLayout(1, 2));
            equiposComboBoxPanel.add(equiposComboBoxLigaUnica1);
            equiposComboBoxPanel.add(equiposComboBoxLigaUnica2);

            // Panel para el campo de texto del código del juego
            JPanel codigoJuegoPanel = new JPanel(new GridLayout(1, 2));
            codigoJuegoPanel.add(new JLabel("Código del Juego:"));
            codigoJuegoPanel.add(codigoJuegoTextFieldLigaUnica);

            // Añadir los paneles al formulario
            formPanel.add(equiposPanel, BorderLayout.NORTH);
            formPanel.add(equiposComboBoxPanel, BorderLayout.CENTER);
            formPanel.add(codigoJuegoPanel, BorderLayout.SOUTH);

            // Agregar el formulario y el botón al panel principal
            panelOrganizarPartidoLigaUnica.add(formPanel, BorderLayout.CENTER);
            panelOrganizarPartidoLigaUnica.add(organizarPartidoButtonLigaUnica, BorderLayout.SOUTH);

            return panelOrganizarPartidoLigaUnica;
        }


        private void cargarEquiposEnComboBox(JComboBox<String> comboBox) {
            try {
                conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Gaming_Leagues", "developer", "23100132");
                stmt = conn.createStatement();
                rs = AplicacionDeportiva.PanelProcesos.stmt.executeQuery("SELECT team_name FROM teams");

                while (AplicacionDeportiva.PanelProcesos.rs.next()) {
                    comboBox.addItem(AplicacionDeportiva.PanelProcesos.rs.getString("team_name"));
                }

                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al cargar equipos desde la base de datos");
            }
        }

        private void cargarJugadoresEnComboBox(JComboBox<String> comboBox) {
            try {
                conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Gaming_Leagues", "developer", "23100132");
                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT first_name, last_name FROM players");

                while (rs.next()) {
                    comboBox.addItem(rs.getString("first_name") + " " + rs.getString("last_name"));
                }

                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al cargar jugadores desde la base de datos");
            }
        }
        // Lógica para asignar jugador seleccionado al equipo seleccionado
        private void asignarJugadorAEquipo(String equipoSeleccionado, String jugadorSeleccionado) {
            try {
                conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Gaming_Leagues", "developer", "23100132");
                PreparedStatement preparedStmt = conn.prepareStatement("INSERT INTO Team_Players (team_id, player_id, date_from) VALUES (?, ?, CURRENT_DATE)");
                preparedStmt.setInt(1, obtenerIdEquipoPorNombre(equipoSeleccionado));
                preparedStmt.setInt(2, obtenerIdJugadorPorNombre(jugadorSeleccionado));
                preparedStmt.executeUpdate();
                conn.close();
                JOptionPane.showMessageDialog(null, "Asignación exitosa");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al asignar jugador al equipo");
            }
        }

        // Obtener ID de equipo por nombre
        private int obtenerIdEquipoPorNombre(String equipoSeleccionado) {
            int teamId = -1;
            try {
                conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Gaming_Leagues", "developer", "23100132");
                PreparedStatement preparedStmt = conn.prepareStatement("SELECT team_id FROM Teams WHERE team_name = ?");
                preparedStmt.setString(1, equipoSeleccionado);
                ResultSet rs = preparedStmt.executeQuery();
                if (rs.next()) {
                    teamId = rs.getInt("team_id");
                }
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return teamId;
        }

        // Obtener ID de jugador por nombre
        private int obtenerIdJugadorPorNombre(String jugadorSeleccionado) {
            int playerId = -1;
            try {
                conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Gaming_Leagues", "developer", "23100132");
                PreparedStatement preparedStmt = conn.prepareStatement("SELECT player_id FROM Players WHERE first_name || ' ' || last_name = ?");
                preparedStmt.setString(1, jugadorSeleccionado);
                ResultSet rs = preparedStmt.executeQuery();
                if (rs.next()) {
                    playerId = rs.getInt("player_id");
                }
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
            return playerId;
        }

        // Método para organizar partido entre jugadores
        private void organizarPartidoEntreJugadores(String jugador1Seleccionado, String jugador2Seleccionado, String codigoJuego) {
            try {
                conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Gaming_Leagues", "developer", "23100132");
                int gameCode = Integer.parseInt(codigoJuego); // Convertir el código de juego a entero
                PreparedStatement preparedStmt = conn.prepareStatement(
                        "INSERT INTO Matches (game_code, player_1_id, player_2_id, match_date) VALUES (?, ?, ?, CURRENT_DATE)");
                preparedStmt.setInt(1, gameCode);
                preparedStmt.setInt(2, obtenerIdJugadorPorNombre(jugador1Seleccionado));
                preparedStmt.setInt(3, obtenerIdJugadorPorNombre(jugador2Seleccionado));
                preparedStmt.executeUpdate();
                conn.close();
                JOptionPane.showMessageDialog(null, "Partido entre jugadores organizado exitosamente");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al organizar el partido entre jugadores", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "El código de juego debe ser un número entero válido", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private void cargarLigasEnComboBox(JComboBox<String> comboBox) {
            try {
                conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Gaming_Leagues", "developer", "23100132");
                stmt = conn.createStatement();
                rs = stmt.executeQuery("SELECT league_name FROM leagues");

                while (rs.next()) {
                    comboBox.addItem(rs.getString("league_name"));
                }

                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al cargar las ligas desde la base de datos");
            }
        }

        private void cargarEquiposDeLigaEnComboBox(String nombreLiga, JComboBox<String> comboBox) {
            try {
                conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Gaming_Leagues", "developer", "23100132");
                PreparedStatement preparedStmt = conn.prepareStatement("SELECT team_name FROM teams WHERE team_id IN (SELECT team_id FROM leagues_teams WHERE league_id = (SELECT league_id FROM leagues WHERE league_name = ?))");
                preparedStmt.setString(1, nombreLiga);
                ResultSet rs = preparedStmt.executeQuery();
                while (rs.next()) {
                    comboBox.addItem(rs.getString("team_name"));
                }
                conn.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al cargar equipos de la liga desde la base de datos");
            }
        }

        private void panelOrganizarPartidoLigas(String liga1Seleccionada, String equipo1Seleccionado, String liga2Seleccionada, String equipo2Seleccionado, String codigoJuego) {
            try {
                // Establecer la conexión con la base de datos
                conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Gaming_Leagues", "developer", "23100132");

                // Obtener la lista de equipos de ambas ligas
                java.util.List<String> equiposLiga1 = obtenerEquiposDeLiga(liga1Seleccionada);
                List<String> equiposLiga2 = obtenerEquiposDeLiga(liga2Seleccionada);

                // Verificar que hay al menos un equipo en cada liga
                if (equiposLiga1.size() < 1 || equiposLiga2.size() < 1) {
                    JOptionPane.showMessageDialog(null, "No hay suficientes equipos en alguna de las ligas seleccionadas para organizar un partido");
                    return;
                }

                // Seleccionar aleatoriamente un equipo de cada liga
                Random random = new Random();
                int indiceEquipo1 = random.nextInt(equiposLiga1.size());
                int indiceEquipo2 = random.nextInt(equiposLiga2.size());

                equipo1Seleccionado = equiposLiga1.get(indiceEquipo1);
                equipo2Seleccionado = equiposLiga2.get(indiceEquipo2);

                // Insertar el partido en la base de datos
                insertarPartidoEnBaseDeDatos(liga1Seleccionada, equipo1Seleccionado, liga2Seleccionada, equipo2Seleccionado, codigoJuego);

                // Cerrar la conexión con la base de datos
                conn.close();

                // Mostrar un mensaje de éxito
                JOptionPane.showMessageDialog(null, "Partido entre equipos de las ligas seleccionadas organizado exitosamente");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al organizar el partido entre equipos de las ligas seleccionadas", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        private List<String> obtenerEquiposDeLiga(String nombreLiga) throws SQLException {
            List<String> equiposLiga = new ArrayList<>();
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Gaming_Leagues", "developer", "23100132");
            PreparedStatement preparedStmt = conn.prepareStatement("SELECT team_name FROM Teams WHERE team_id IN (SELECT team_id FROM leagues_teams WHERE league_id = (SELECT league_id FROM Leagues WHERE league_name = ?))");
            preparedStmt.setString(1, nombreLiga);
            ResultSet rs = preparedStmt.executeQuery();
            while (rs.next()) {
                equiposLiga.add(rs.getString("team_name"));
            }
            conn.close();
            return equiposLiga;
        }

        private void insertarPartidoEnBaseDeDatos(String liga1Seleccionada, String equipo1Seleccionado, String liga2Seleccionada, String equipo2Seleccionado, String codigoJuego) {
            try {
                conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Gaming_Leagues", "developer", "23100132");
                PreparedStatement preparedStmt = conn.prepareStatement(
                        "INSERT INTO Matches_Teams (game_code, team1_id, team2_id, match_date) VALUES (?, ?, ?, CURRENT_DATE)");
                preparedStmt.setInt(1, Integer.parseInt(codigoJuego)); // Usar el código de juego proporcionado
                preparedStmt.setInt(2, obtenerIdEquipoPorNombre(equipo1Seleccionado));
                preparedStmt.setInt(3, obtenerIdEquipoPorNombre(equipo2Seleccionado));
                preparedStmt.executeUpdate();
                conn.close();
                JOptionPane.showMessageDialog(null, "Partido entre equipos de las ligas seleccionadas organizado exitosamente");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al organizar el partido entre equipos de las ligas seleccionadas", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "El código de juego debe ser un número entero válido", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

        // Método para organizar partido entre equipos de la misma liga
        private void organizarPartidoEntreEquiposDeLiga(String ligaSeleccionada, String equipo1Seleccionado, String equipo2Seleccionado, String codigoJuego) {
            try {
                conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Gaming_Leagues", "developer", "23100132");
                int gameCode = Integer.parseInt(codigoJuego); // Obtener el código del juego proporcionado
                PreparedStatement preparedStmt = conn.prepareStatement(
                        "INSERT INTO Matches_Teams (game_code, team1_id, team2_id, match_date) VALUES (?, ?, ?, CURRENT_DATE)");
                preparedStmt.setInt(1, gameCode);
                preparedStmt.setInt(2, obtenerIdEquipoPorNombre(equipo1Seleccionado));
                preparedStmt.setInt(3, obtenerIdEquipoPorNombre(equipo2Seleccionado));
                preparedStmt.executeUpdate();
                conn.close();
                JOptionPane.showMessageDialog(null, "Partido entre equipos de la misma liga organizado exitosamente");
            } catch (SQLException ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(null, "Error al organizar el partido entre equipos de la misma liga", "Error", JOptionPane.ERROR_MESSAGE);
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(null, "El código de juego debe ser un número entero válido", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }

    }

    // Clase para crear el panel de Reportes
    static class PanelReportes extends JPanel {
        private JButton jugadoresDestacadosBtn;
        private JButton rendimientoEquiposBtn;
        private JButton historialPartidosBtn;
        private JButton historialPartidosEquipoBtn;

        public PanelReportes() {
            setLayout(new GridBagLayout());
            GridBagConstraints gbc = new GridBagConstraints();
            gbc.fill = GridBagConstraints.HORIZONTAL;
            gbc.insets = new Insets(10, 10, 10, 10);

            // Estilo del panel
            setBackground(Color.WHITE);

            // Título del panel
            JLabel titleLabel = new JLabel("Gestión de Reportes");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            add(titleLabel, gbc);

            // Botón Reporte: Jugadores Destacados
            jugadoresDestacadosBtn = new JButton("Jugadores Destacados en Cada Juego");
            gbc.gridwidth = 1;
            gbc.gridx = 0;
            gbc.gridy = 1;
            add(jugadoresDestacadosBtn, gbc);

            // Descripción del reporte
            JLabel jugadoresDesc = new JLabel("Genera un reporte de los jugadores destacados en cada juego.");
            gbc.gridx = 1;
            gbc.gridy = 1;
            add(jugadoresDesc, gbc);

            jugadoresDestacadosBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    generarReporteJugadoresDestacados();
                }
            });

            // Botón Reporte: Rendimiento de Equipos
            rendimientoEquiposBtn = new JButton("Rendimiento de Equipos en las Ligas");
            gbc.gridx = 0;
            gbc.gridy = 2;
            add(rendimientoEquiposBtn, gbc);

            // Descripción del reporte
            JLabel rendimientoDesc = new JLabel("Genera un reporte sobre el rendimiento de los equipos en las ligas.");
            gbc.gridx = 1;
            gbc.gridy = 2;
            add(rendimientoDesc, gbc);

            rendimientoEquiposBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    generarReporteRendimientoEquipos();
                }
            });

            // Botón Reporte: Historial de Partidos de Jugadores
            historialPartidosBtn = new JButton("Historial de Partidos de Cada Jugador");
            gbc.gridx = 0;
            gbc.gridy = 3;
            add(historialPartidosBtn, gbc);

            // Descripción del reporte
            JLabel historialJugadoresDesc = new JLabel("Genera un reporte del historial de partidos de cada jugador.");
            gbc.gridx = 1;
            gbc.gridy = 3;
            add(historialJugadoresDesc, gbc);

            historialPartidosBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    generarReporteHistorialPartidos();
                }
            });

            // Botón Reporte: Historial de Partidos de Equipo
            historialPartidosEquipoBtn = new JButton("Historial de Partidos de Equipo");
            gbc.gridx = 0;
            gbc.gridy = 4;
            add(historialPartidosEquipoBtn, gbc);

            // Descripción del reporte
            JLabel historialEquipoDesc = new JLabel("Genera un reporte del historial de partidos de un equipo.");
            gbc.gridx = 1;
            gbc.gridy = 4;
            add(historialEquipoDesc, gbc);

            historialPartidosEquipoBtn.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    generarReporteHistorialPartidosEquipo();
                }
            });
        }

        // Método para generar el reporte de jugadores más destacados en cada juego
        private void generarReporteJugadoresDestacados() {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("reporte_jugadores_destacados.txt"))) {
                Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Gaming_Leagues", "developer", "23100132");
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT g.game_name, p.first_name, p.last_name FROM Players p INNER JOIN Players_Game_Ranking pr ON p.player_id = pr.player_id INNER JOIN Games g ON pr.game_code = g.game_code ORDER BY pr.ranking DESC");

                while (rs.next()) {
                    String juego = rs.getString("game_name");
                    String jugador = rs.getString("first_name") + " " + rs.getString("last_name");
                    writer.write("Juego: " + juego + ", Jugador destacado: " + jugador);
                    writer.newLine();
                }

                conn.close();
            } catch (SQLException | IOException ex) {
                ex.printStackTrace();
            }
        }

        // Método para generar el reporte de rendimiento de equipos en las ligas
        private void generarReporteRendimientoEquipos() {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("reporte_rendimiento_equipos.txt"))) {
                Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Gaming_Leagues", "developer", "23100132");
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT l.league_name, t.team_name, COUNT(mt.match_id) AS matches_played, SUM(CASE WHEN mt.result = 'win' THEN 1 ELSE 0 END) AS wins, SUM(CASE WHEN mt.result = 'lose' THEN 1 ELSE 0 END) AS losses FROM Matches_Teams mt INNER JOIN Leagues l ON mt.league_id = l.league_id INNER JOIN Teams t ON mt.team1_id = t.team_id OR mt.team2_id = t.team_id GROUP BY l.league_name, t.team_name");

                while (rs.next()) {
                    String liga = rs.getString("league_name");
                    String equipo = rs.getString("team_name");
                    int victorias = rs.getInt("wins");
                    int derrotas = rs.getInt("losses");
                    writer.write("Liga: " + liga + ", Equipo: " + equipo + ", Victorias: " + victorias + ", Derrotas: " + derrotas);
                    writer.newLine();
                }

                conn.close();
            } catch (SQLException | IOException ex) {
                ex.printStackTrace();
            }
        }

        // Método para generar el reporte de historial de partidos de cada jugador
        private void generarReporteHistorialPartidos() {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("reporte_historial_partidos_jugador.txt"))) {
                Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Gaming_Leagues", "developer", "23100132");
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT m.match_date, CONCAT(p1.first_name, ' ', p1.last_name) AS player1, CONCAT(p2.first_name, ' ', p2.last_name) AS player2, m.result FROM Matches m INNER JOIN Players p1 ON m.player_1_id = p1.player_id INNER JOIN Players p2 ON m.player_2_id = p2.player_id");

                while (rs.next()) {
                    Date fecha = rs.getDate("match_date");
                    String jugador1 = rs.getString("player1");
                    String jugador2 = rs.getString("player2");
                    String resultado = rs.getString("result");
                    writer.write("Fecha del partido: " + fecha + ", Jugador 1: " + jugador1 + ", Jugador 2: " + jugador2 + ", Resultado: " + resultado);
                    writer.newLine();
                }

                conn.close();
            } catch (SQLException | IOException ex) {
                ex.printStackTrace();
            }
        }

        // Método para generar el reporte de historial de partidos de equipo
        private void generarReporteHistorialPartidosEquipo() {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter("reporte_historial_partidos_equipo.txt"))) {
                Connection conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/Gaming_Leagues", "developer", "23100132");
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery("SELECT mt.match_date, t1.team_name AS team1, t2.team_name AS team2, mt.result FROM Matches_Teams mt INNER JOIN Teams t1 ON mt.team1_id = t1.team_id INNER JOIN Teams t2 ON mt.team2_id = t2.team_id");

                while (rs.next()) {
                    Date fecha = rs.getDate("match_date");
                    String equipo1 = rs.getString("team1");
                    String equipo2 = rs.getString("team2");
                    String resultado = rs.getString("result");
                    writer.write("Fecha del partido: " + fecha + ", Equipo 1: " + equipo1 + ", Equipo 2: " + equipo2 + ", Resultado: " + resultado);
                    writer.newLine();
                }

                conn.close();
            } catch (SQLException | IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
