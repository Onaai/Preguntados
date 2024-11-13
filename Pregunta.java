import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Pregunta {
    private String texto;
    private String[] opciones;
    private String[] respuestasCorrectas;
    private String propietario;
    private boolean esMultiple;

    public Pregunta(String texto, String[] opciones, String[] respuestasCorrectas, String propietario, boolean esMultiple) {
        this.texto = texto;
        this.opciones = opciones;
        this.respuestasCorrectas = respuestasCorrectas;
        this.propietario = propietario;
        this.esMultiple = esMultiple;
    }

    public String getTexto() {
        return texto;
    }

    public String[] getOpciones() {
        return opciones;
    }

    public String getPropietario() {
        return propietario;
    }

    public boolean esMultiple() {
        return esMultiple;
    }

    public boolean esCorrecta(String respuesta) {
        for (String correcta : respuestasCorrectas) {
            if (correcta.equalsIgnoreCase(respuesta)) {
                return true;
            }
        }
        return false;
    }

    public boolean sonCorrectas(String[] respuestasSeleccionadas) {
        Set<String> correctasSet = new HashSet<>(Arrays.asList(respuestasCorrectas));
        Set<String> seleccionadasSet = new HashSet<>(Arrays.asList(respuestasSeleccionadas));
        return correctasSet.equals(seleccionadasSet);
    }

    @Override
    public String toString() {
        return "Pregunta: " + texto + "\nOpciones: " + Arrays.toString(opciones) + "\nPropietario: " + propietario;
    }
}
