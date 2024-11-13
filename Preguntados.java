import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class Preguntados extends JFrame {
    private List<Pregunta> preguntas;
    private int indicePregunta;
    private JCheckBox[] checkBoxes;
    private JButton btnSiguiente;
    private JLabel lblPregunta;
    private JLabel lblPropietario;
    private JLabel lblContador;
    private JLabel lblTimer;
    private Timer timer;
    private int tiempoRestante;
    private final int TIEMPO_LIMITE = 30; // 30 segundos por pregunta

    public Preguntados() {
        preguntas = seleccionarPreguntasAleatorias(cargarPreguntas(), 10);
        indicePregunta = 0;
        inicializarUI();
        mostrarPregunta();
    }

    private void inicializarUI() {
        setTitle("Juego de Preguntas");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(9, 1));

        lblContador = new JLabel();
        lblPregunta = new JLabel();
        lblPropietario = new JLabel();
        lblTimer = new JLabel();
        add(lblContador);
        add(lblPregunta);
        add(lblPropietario);
        add(lblTimer);

        checkBoxes = new JCheckBox[4];
        for (int i = 0; i < checkBoxes.length; i++) {
            checkBoxes[i] = new JCheckBox();
            add(checkBoxes[i]);
        }

        btnSiguiente = new JButton("Siguiente");
        btnSiguiente.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                detenerTimer();
                verificarRespuesta();
            }
        });
        add(btnSiguiente);
    }

    private List<Pregunta> cargarPreguntas() {
        List<Pregunta> listaPreguntas = new ArrayList<>();

        // Ejemplo de preguntas
        listaPreguntas.add(new Pregunta("¿Qué usos tiene el hashing? (MÚLTIPLES OPCIONES CORRECTAS)",
                new String[]{"Búsqueda de datos existentes", "Compresión de archivos", "Verificación de integridad", "Encriptación de datos"},
                new String[]{"Búsqueda de datos existentes", "Verificación de integridad"}, "Tomas", true));

        listaPreguntas.add(new Pregunta("¿Para qué sirve java.util.Scanner?",
                new String[]{"Para realizar operaciones matemáticas complejas", "Para leer datos de entrada desde fuentes como el teclado, archivos o cadenas",
                        "Para gestionar las excepciones en el programa", "Para generar números aleatorios de forma eficiente"},
                new String[]{"Para leer datos de entrada desde fuentes como el teclado, archivos o cadenas"}, "Joa", false));

        listaPreguntas.add(new Pregunta("¿Qué es Spring?",
                new String[]{"Una librería", "Ninguna es correcta", "Un framework", "Un programa"},
                new String[]{"Un framework"}, "Emiliano", false));

        listaPreguntas.add(new Pregunta("¿Cuáles son los principios del POO?",
                new String[]{"Polimorfismo y todos los restantes", "Abstracción", "Encapsulación", "Herencia"},
                new String[]{"Polimorfismo y todos los restantes"}, "Emiliano", true));

        // Mezclar preguntas
        Collections.shuffle(listaPreguntas);

        return listaPreguntas;
    }

    private List<Pregunta> seleccionarPreguntasAleatorias(List<Pregunta> preguntas, int cantidad) {
        Collections.shuffle(preguntas);
        return preguntas.subList(0, Math.min(cantidad, preguntas.size()));
    }

    private void mostrarPregunta() {
        if (indicePregunta < preguntas.size()) {
            iniciarTimer();
            Pregunta preguntaActual = preguntas.get(indicePregunta);
            lblContador.setText("Pregunta " + (indicePregunta + 1) + " de 10");
            lblPregunta.setText(preguntaActual.getTexto());
            lblPropietario.setText("Propietario: " + preguntaActual.getPropietario());

            String[] opciones = preguntaActual.getOpciones();
            boolean esMultiple = preguntaActual.esMultiple();

            // Habilitar o deshabilitar opciones según si la pregunta permite múltiples respuestas
            for (int i = 0; i < checkBoxes.length; i++) {
                if (i < opciones.length) {
                    checkBoxes[i].setText(opciones[i]);
                    checkBoxes[i].setVisible(true);
                    checkBoxes[i].setEnabled(true); // Asegura que las opciones siempre sean seleccionables
                    checkBoxes[i].setSelected(false); // Deseleccionamos las opciones al mostrar una nueva pregunta
                    // Si la pregunta es de opción única, usamos JRadioButton
                    if (!esMultiple) {
                        checkBoxes[i].setEnabled(true); // Las opciones de selección única se pueden elegir
                    }
                } else {
                    checkBoxes[i].setVisible(false);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "¡Fin del juego!");
            System.exit(0);
        }
    }

    private void iniciarTimer() {
        tiempoRestante = TIEMPO_LIMITE;
        lblTimer.setText("Tiempo restante: " + tiempoRestante + " segundos");

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                tiempoRestante--;
                lblTimer.setText("Tiempo restante: " + tiempoRestante + " segundos");
                if (tiempoRestante <= 0) {
                    detenerTimer();
                    verificarRespuesta();
                }
            }
        }, 1000, 1000);
    }

    private void detenerTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }

    private void verificarRespuesta() {
        Pregunta preguntaActual = preguntas.get(indicePregunta);
        List<String> respuestasSeleccionadas = new ArrayList<>();

        for (JCheckBox checkBox : checkBoxes) {
            if (checkBox.isSelected()) {
                respuestasSeleccionadas.add(checkBox.getText());
            }
        }

        if (preguntaActual.sonCorrectas(respuestasSeleccionadas.toArray(new String[0]))) {
            JOptionPane.showMessageDialog(this, "¡Respuesta correcta!");
        } else {
            JOptionPane.showMessageDialog(this, "Respuesta incorrecta.");
        }

        // Avanzamos a la siguiente pregunta sin terminar el juego
        indicePregunta++;
        mostrarPregunta();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Preguntados().setVisible(true));
    }
}
