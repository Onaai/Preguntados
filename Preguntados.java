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
    private JLabel lblPuntos;
    private Timer timer;
    private int tiempoRestante;
    private final int TIEMPO_LIMITE = 30;
    private int puntos;

    public Preguntados() {
        preguntas = seleccionarPreguntasAleatorias(cargarPreguntas(), 10);
        indicePregunta = 0;
        puntos = 0;
        inicializarUI();
        mostrarPregunta();
    }

    private void inicializarUI() {
        setTitle("Juego de Preguntas");
        setSize(400, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridLayout(10, 1));

        lblContador = new JLabel();
        lblPregunta = new JLabel();
        lblPropietario = new JLabel();
        lblTimer = new JLabel();
        lblPuntos = new JLabel("Puntos: 0");
        add(lblContador);
        add(lblPregunta);
        add(lblPropietario);
        add(lblTimer);
        add(lblPuntos);

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

        listaPreguntas.add(new Pregunta("¿Qué usos tiene el hashing? (MÚLTIPLES OPCIONES CORRECTAS)",
                new String[]{"Búsqueda de datos existentes", "Compresión de archivos", "Verificación de integridad", "Encriptación de datos"},
                new String[]{"Búsqueda de datos existentes", "Verificación de integridad"}, "Tomas", true));

        listaPreguntas.add(new Pregunta("¿Para qué sirve java.util.Scanner?",
                new String[]{"Para realizar operaciones matemáticas complejas", "Para leer datos de entrada desde fuentes como el teclado, archivos o cadenas",
                        "Para gestionar las excepciones en el programa", "Para generar números aleatorios de forma eficiente"},
                new String[]{"Para leer datos de entrada desde fuentes como el teclado, archivos o cadenas"}, "Nahuel Iriart", false));

        listaPreguntas.add(new Pregunta("¿Qué es Spring?",
                new String[]{"Una librería", "Ninguna es correcta", "Un framework", "Un programa"},
                new String[]{"Un framework"}, "Emiliano", false));

        listaPreguntas.add(new Pregunta("¿Cuáles son los principios del POO?",
                new String[]{"Polimorfismo y todos los restantes", "Abstracción", "Encapsulación", "Herencia"},
                new String[]{"Polimorfismo y todos los restantes"}, "Emiliano", true));

        // Nuevas preguntas
        listaPreguntas.add(new Pregunta("¿Cuál de los siguientes es un tipo de dato primitivo?",
                new String[]{"String", "Integer", "int", "Array"},
                new String[]{"int"}, "Mica", false));

        listaPreguntas.add(new Pregunta("¿Cuál es el proceso de convertir un tipo de dato a otro en Java?",
                new String[]{"Casting", "Boxing", "Parsing", "Wrapper"},
                new String[]{"Casting"}, "Mica", false));

        listaPreguntas.add(new Pregunta("¿Cuál de las siguientes es una estructura de datos dinámica en Java?",
                new String[]{"int", "ArrayList", "String", "Double"},
                new String[]{"ArrayList"}, "Mica", false));

        listaPreguntas.add(new Pregunta("¿Para qué se usa java.util.Enumeration en Java?",
                new String[]{"Para representar colecciones de tipo Map", "Para iterar elementos en un tipo de colección", "Para ordenar listas", "Para almacenar valores únicos"},
                new String[]{"Para iterar elementos en un tipo de colección"}, "Mica", false));

        listaPreguntas.add(new Pregunta("¿Con qué comando se agrega un archivo a GitHub?",
                new String[]{"Git init", "Git add", "Git commit", "Git branch"},
                new String[]{"Git add"}, "Nahuel Iriart", false));

        listaPreguntas.add(new Pregunta("¿Para qué sirve el SecureRandom?",
                new String[]{"Generar contraseñas de longitud fija sin ningún tipo de aleatoriedad",
                        "Crear claves de cifrado fijas que no cambian",
                        "Proveer números aleatorios con un nivel de seguridad adecuado para criptografía",
                        "Generar números aleatorios de manera predecible"},
                new String[]{"Proveer números aleatorios con un nivel de seguridad adecuado para criptografía"}, "Nahuel Iriart", false));

        listaPreguntas.add(new Pregunta("¿Cómo se declara un JFrame?",
                new String[]{"JFrame frame = new JFrame();", "public class MiVentana extends JFrame", "Ambas son válidas", "Ninguna es correcta"},
                new String[]{"public class MiVentana extends JFrame"}, "Joaco", false));

        listaPreguntas.add(new Pregunta("¿Cómo se le pone un valor a un gráfico de torta?",
                new String[]{"Con chocolate", ".setValue()", ".pack()", "Se colocan al declarar el gráfico"},
                new String[]{".setValue()"}, "Joaco", false));

        listaPreguntas.add(new Pregunta("¿Cuál es el nombre de la librería para importar un gráfico?",
                new String[]{"javax.swing", "java.chart", "java.awt", "javax.graphics"},
                new String[]{"java.awt"}, "Joaco", false));


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
            lblPropietario.setText("Portfolio de: " + preguntaActual.getPropietario());

            String[] opciones = preguntaActual.getOpciones();
            boolean esMultiple = preguntaActual.esMultiple();

            for (int i = 0; i < checkBoxes.length; i++) {
                if (i < opciones.length) {
                    checkBoxes[i].setText(opciones[i]);
                    checkBoxes[i].setVisible(true);
                    checkBoxes[i].setEnabled(true);
                    checkBoxes[i].setSelected(false);
                    if (!esMultiple) {
                        checkBoxes[i].setEnabled(true);
                    }
                } else {
                    checkBoxes[i].setVisible(false);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "¡Fin del juego! Puntuación final: " + puntos);
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
                    JOptionPane.showMessageDialog(Preguntados.this, "El tiempo se agotó");
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

        boolean respuestaCorrecta = preguntaActual.sonCorrectas(respuestasSeleccionadas.toArray(new String[0]));

        if (respuestaCorrecta) {
            puntos++;
            JOptionPane.showMessageDialog(this, "¡Respuesta correcta!");
        } else {
            puntos--;
            JOptionPane.showMessageDialog(this, "La respuesta es incorrecta");
        }

        lblPuntos.setText("Puntos: " + puntos);
        indicePregunta++;
        mostrarPregunta();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Preguntados().setVisible(true));
    }
}
