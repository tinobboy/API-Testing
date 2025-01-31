package helpers;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.*;

public class NodeHelper {
    XPath xPath;
    Node node;

    public NodeHelper() {
        XPathFactory xPathfactory = XPathFactory.newInstance();
        this.xPath = xPathfactory.newXPath();
    }

    public NodeHelper(Node node){
        this.node=node;
        XPathFactory xPathfactory = XPathFactory.newInstance();
        this.xPath = xPathfactory.newXPath();
    }

    public String nodeToString(Node node) {
        StringWriter sw = new StringWriter();
        try {
            Transformer t = TransformerFactory.newInstance().newTransformer();
            t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            //t.setOutputProperty(OutputKeys.INDENT, "yes");
            t.transform(new DOMSource(node), new StreamResult(sw));
        } catch (TransformerException e) {
            throw new IllegalArgumentException("Error en el parseo del Nodo a String: " + e.getMessage());
        }
        return sw.toString();
    }

    public Node stringToNode(String string) {
        Node node;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new InputSource(new StringReader(string)));
            node = (Node) xPath.compile("*").evaluate(document, XPathConstants.NODE);
        } catch (ParserConfigurationException e)
            { throw new IllegalArgumentException("Error al crear DocumentBuilder: " + e.getMessage()); }
        catch (IOException e)
            { throw new IllegalArgumentException("Error al parsear String a Document (IOException): " + e.getMessage()); }
        catch (SAXException e)
            { throw new IllegalArgumentException("Error al parsear String a Document (SAXException): " + e.getMessage()); }
        catch (XPathExpressionException e)
            { throw new IllegalArgumentException("Error al parsear Document a Node: " + e.getMessage()); }

        return node;
    }

    /**
     * Devuelve un Node a partir de un Node
     * @param body
     * @return
     */
    public Node getNode(String node, Node body){
        Node resultNode = null;
        try {
            resultNode = (Node) xPath.compile("*//" + node).evaluate(body, XPathConstants.NODE);
        } catch (XPathExpressionException e) {
            throw new IllegalArgumentException("Error al obtener nodo: " + e.getMessage());
        }
        return resultNode;
    }

    /**
     * Devuelve un NodeList a partir de un Node
     * @param body
     * @return
     */
    public NodeList getNodeList(String node, Node body){
        NodeList resultNodeList = null;
        try {
            resultNodeList = (NodeList) xPath.compile("*//" + node).evaluate(body, XPathConstants.NODESET);
        } catch (XPathExpressionException e) {
            throw new IllegalArgumentException("Error al obtener lista de nodos: " + e.getMessage());
        }
        return resultNodeList;
    }

    /**
     * Devuelve un Node a partir del path de un archivo
     * @param path del File con contenido en formato XML
     * @return
     */
    public Node getNodeFromFile(String path){
        File file = new File(path);
        canReadFile(file);
        Node node;

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dbBuilder = factory.newDocumentBuilder();
            Document document = dbBuilder.parse(new FileInputStream(file));
            node = (Node) xPath.compile("*").evaluate(document, XPathConstants.NODE);
        } catch (ParserConfigurationException e)
            { throw new IllegalArgumentException("Error al crear DocumentBuilder: " + e.getMessage()); }
        catch (IOException e)
            { throw new IllegalArgumentException("Error al parsear File a Document (IOException): " + e.getMessage()); }
        catch (SAXException e)
            { throw new IllegalArgumentException("Error al parsear File a Document (SAXException): " + e.getMessage()); }
        catch (XPathExpressionException e)
            { throw new IllegalArgumentException("Error al parsear Document a Node: " + e.getMessage()); }

        return node;
    }

    public String getValueFromNode(String campo){
        Element el = (Element) node;
        return el.getElementsByTagName(campo).item(0).getTextContent();
    }

    public String getValueFromNode(String campo, Node node){
        Element el = (Element) node;
        return el.getElementsByTagName(campo).item(0).getTextContent();
    }

    /**
     * Si el File no se puede leer, lanza un IllegalArgumentException
     * @param file
     */
    private void canReadFile(File file){
        if(!file.exists()) throw new IllegalArgumentException("No existe el archivo: " + file.getPath());
        if(!file.canRead()) throw new IllegalArgumentException("No se pudo leer el archivo: " + file.getPath());
    }

}
