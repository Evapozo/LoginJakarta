package es.daw.loginjakarta.util;

import es.daw.loginjakarta.exception.TxtNoEncontradoException;
import jakarta.servlet.ServletContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    //meter metodo leer fichero

    /**
     * Lee un fichero de texto
     * @param pathFile ruta al fichero. Debe ser absoluta y encontrarse protegida en WEB-INF
     * @return List de cadena de texto de cada linea
     * @throws IOException si no existe la ruta
     */
    private static ServletContext context;
    public static List<String> leerFichero(String pathFile) throws IOException, TxtNoEncontradoException{
        List<String> lista = new ArrayList<>();

        // getResourceAsStream abre un flujo de bytes (InputStream)
        InputStream is = context.getResourceAsStream(pathFile);

        // En vez de propagar IOException, implementar una excepción propia de tipo checked
        // llamada FicheroTxtNoEncontradoException...
        if ( is == null)
            //throw new IOException("No se encuentra el fichero de texto: "+pathFile);
        try {
            throw new TxtNoEncontradoException("No se encuentra el fichero de texto: " + pathFile);
        } catch (TxtNoEncontradoException e) {
            throw new RuntimeException(e);
        }


        // try con recursos: todo lo que se declara dentro del paréntesis se cierra automáticamente (close())
        // InputStream -> bytes en crudo
        // InputStreamReader -> convierte esos bytes en caracteres según el charset
        // BufferedReader -> añade un buffer para leer línea a línea
        try(BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))){
            String linea;
            while( (linea = br.readLine()) != null){
                if (!linea.isBlank())
                    //lista.add(linea.trim());
                    lista.add(linea.strip());

            }
        }
        return lista;
    }
}
