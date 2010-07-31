package org.mnode.juicer.slurper


import groovy.lang.GroovyObjectSupport;

class JcrNodeGPath extends GroovyObjectSupport {
    
    private final javax.jcr.Node node;
    
    JcrNodeGPath(javax.jcr.Node node) {
        this.node = node
    }
    
    @Override
    public Object getProperty(String name) {
        if (name.startsWith("@") && node.hasProperty(name.substring(1))) {
            return node.getProperty(name.substring(1))
        }
        else if (node.hasNode(name)) {
            return new JcrNodeGPath(node.getNode(name))
        }
        return null
    }
    
    @Override
    public void setProperty(String name, newValue) {
        if (!node.hasProperty(name) || node.getProperty(name).string != node.session.valueFactory.createValue(newValue).string) {
//            println "prop ${name} - new value: ${newValue}"
            node.setProperty name, newValue
        }
    }
    
    @Override
    String toString() {
        node.path
    }
}
