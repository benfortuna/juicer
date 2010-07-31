package org.mnode.juicer.slurper


import groovy.lang.GroovyObjectSupport;

class JcrNodeGPath extends GroovyObjectSupport {
    
    private final javax.jcr.Node node;
    
    JcrNodeGPath(javax.jcr.Node node) {
        this.node = node
    }
    
    public Object getProperty(String name) {
        if (name.startsWith("@") && node.hasProperty(name.substring(1))) {
            return node.getProperty(name.substring(1))
        }
        else if (node.hasNode(name)) {
            return new JcrNodeGPath(node.getNode(name))
        }
        return null
    }
    
    public void setProperty(String name, Object newValue) {
        node.setProperty name, newValue
    }
    
    public void setProperty(String propertyName, String newValue) {
        if (!node.hasProperty(propertyName) || node.getProperty(propertyName).string != newValue) {
            node.setProperty propertyName, newValue
        }
    }
    
    String toString() {
        node.path
    }
}
