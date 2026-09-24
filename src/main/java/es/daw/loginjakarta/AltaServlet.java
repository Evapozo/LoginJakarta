package es.daw.loginjakarta;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;


@WebServlet("/alta")
public class AltaServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(AltaServlet.class.getName());

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException{
        try {
            List<String> tecnologias = leerFichero("WEB-INF/datos/tecnologias.txt");
            LOGGER.info(tecnologias.toString());

            //los parametros via get si no viajan llegan como null!!
            //si hago trim de opcional y no se ha enviado en la url como parametro, dara nullpointerexception

            //String opcional = request.getParameter("opcional").trim();

            request.setAttribute("tecnologias", tecnologias);
            //lamar a JSP
            request.getRequestDispatcher("/formulario.jsp").forward(request, response);

        }catch (IOException e){
            //Enviar a una página ERROR.JSP de error el mensaje de error...(crear clase)
            //usa logger para mostrar trazas
            LOGGER.severe(e.getMessage());
            //añadir como atributo el mensaje de error
            request.setAttribute("mensajeError", e.getMessage());

            //reedirigir a la jsp (necesita propagacion IOException
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        //leer todos los parametros del formulario
        String nombre = request.getParameter("nombre");
        String email= request.getParameter("email");
        String tecnologia= request.getParameter("tecnologia");
        String nivel= request.getParameter("nivel");

        //EN ESTE PUNTO SE COMPROBARIA EN BD SI EXISTE UN USARIO CON ESE NOMBRE
        //validar parametros
        nombre = nombre == null ? null : nombre.trim();
        //los parametros no se rellenan llegan vacios
        email = email == null ? null : email.trim();
        tecnologia = tecnologia == null ? null : tecnologia.trim();
        nivel = nivel == null ? null : nivel.trim();

        LOGGER.info("nombre" + nombre);
        LOGGER.info(String.format("email: %s", email));
        LOGGER.info(String.format("tecnologia: %s", tecnologia));
        LOGGER.info(String.format("nivel: %s", nivel));

        if (nombre == null || nombre.isEmpty()) {
            request.setAttribute("mensaje", "El nombre es obligatorio.");
            request.getRequestDispatcher("formulario.jsp").forward(request, response);
            return;
        }
        //PENDIENTE si el nombre viene vacioque vuelva a la pagina del formulario indicando
        //que el nombre nopuede estar vacio
        //enviar a la JSP como atributos los paramteros
        request.setAttribute("nombre", nombre);
        request.setAttribute("email", email);
        request.setAttribute("tecnologia", tecnologia);
        request.setAttribute("nivel", nivel);

        //llamar a la pagina CONFIRMACION.JSP

        request.getRequestDispatcher("/confirmacion.jsp").forward(request, response);



    }

    /**
     * Lee un fichero de texto
     * @param pathFile ruta al fichero. Debe ser absoluta y encontrarse protegida en WWEB-INF
     * @return lista de String de cada línea
     * @throws IOException
     *
     */

    private List<String> leerFichero(String pathFile) throws IOException {
        List<String> lista = new ArrayList<>();

        //getREsourceAsStream abre un flujo de bytes (InputStream)
        InputStream is = getServletContext().getResourceAsStream(pathFile);

        //PENDIENTE!! En vez de propagar IOException, implementar una excepcion propia de tipo checked
        //llamada FicheroTxtNoEncontradoException
        if (is == null)
            throw new IOException("No se encuentra el fichero de texto: " + pathFile);

        //try con recursos: todo lo que se declara dentro del parentesis se cierra automaticamente (close())
        //buffered reader fuera del try hay que cerrarlo
        //InputStream: bytes en crudo
        //InputStreamReader: convierte esos bytes en caracteres segun el charset
        //BufferedReader: añade un bufefr para leer linea a linea
        try (BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8))) {
            String linea;
            while((linea = br.readLine()) != null){
                if(!linea.isBlank())
                    //lista.add(linea.trim());
                    lista.add(linea.strip());


            }

        }
        return lista;
    }


}