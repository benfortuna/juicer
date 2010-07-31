package org.mnode.juicer

import javax.jcr.Node;
import javax.jcr.NodeIterator;

class JcrNodeCategory {

//    static def propertyMissing(Node node, String propertyName) {
    static javax.jcr.Property get(Node node, String propertyName) {
        if (node.hasProperty(propertyName)) {
            return node.getProperty(propertyName)
        }
        return null
    }
    
    static Node getAt(Node node, int index) {
        if (node.nodes.size() >= index - 1) {
            NodeIterator children = node.nodes
            children.skip index
            return children.nextNode()
        }
        return null
    }

//    static def propertyMissing(Node node, String propertyName, value) {
//    static void setProperty(Node node, String propertyName, value) {
//    static void put(Node node, String propertyName, value) {
//        node.setProperty propertyName, value
//    }
}
