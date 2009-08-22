/*
 *
 *
 */
package org.matheclipse.core.form.mathml;
import java.io.IOException;
import java.io.StringBufferInputStream;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
/**
 * Check if a given string is consistent with the MathML DTD
 *
 */
public class MathMLDTDCheck {
    class SAXHandler extends DefaultHandler {
        boolean name = false;
//        boolean syntax = false;
        ArrayList<String> aList;
        StringBuffer stringBuffer;

        public SAXHandler() {
            super();
            aList = new ArrayList<String>();
        }
        public void startElement(String nsURI, String strippedName, String tagName, Attributes attributes) {
            stringBuffer = new StringBuffer();
//            if (tagName.equalsIgnoreCase("syntax")) {
//                syntax = true;
//            }
        }

        public void endElement(String uri, String localName, String qName) throws SAXException {
//            if (syntax) {
                aList.add(stringBuffer.toString());
//                syntax = false;
//            }
        }

        public void characters(char[] ch, int start, int length) {
            stringBuffer.append(ch, start, length);
        }

        public ArrayList getList() {
            return aList;
        }
        /* (non-Javadoc)
         * @see org.xml.sax.DTDHandler#notationDecl(java.lang.String, java.lang.String, java.lang.String)
         */
        public void notationDecl(String name, String publicId, String systemId) throws SAXException {
            // TODO Auto-generated method stub
            super.notationDecl(name, publicId, systemId);
        }

        /* (non-Javadoc)
         * @see org.xml.sax.DTDHandler#unparsedEntityDecl(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
         */
        public void unparsedEntityDecl(String name, String publicId, String systemId, String notationName) throws SAXException {
            // TODO Auto-generated method stub
            super.unparsedEntityDecl(name, publicId, systemId, notationName);
        }

    }

    public void test(String test) {
        //  StringReader is = new StringReader(test);
        StringBufferInputStream sis = new StringBufferInputStream(test);
        //    XmlDocument doc = XmlDocument.createXmlDocument(sis,true);
        SAXParserFactory factory = SAXParserFactory.newInstance();
        factory.setValidating(true);
        try {
            SAXParser saxParser = factory.newSAXParser();
            saxParser.setProperty("", "");
            SAXHandler sh = new SAXHandler();
            saxParser.parse(sis, sh);
            ArrayList aList = sh.getList();
            for (int i = 0; i < aList.size(); i++) {
                System.out.println(aList.get(i));
            }
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException se) {
            se.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
    public static void main(String args[]) {
        String test = new String("<math></math>");
        new MathMLDTDCheck().test(test);
    }
}
