# Introduction #

The following extensions enhance the functionality of javax.jcr.Node implementations when accessed from within Groovy.


# Property Access #

```
def node = session.rootNode.addNode('testNode')
node['testProperty'] = 'testValue'
		
assert node['testProperty'].string == 'testValue'
```


# Renaming Nodes #

```
def node = session.rootNode.addNode('testRenameNode')

node.rename('testNodeIsRenamed')
assert session.getNode('/testNodeIsRenamed')
```

# Moving Nodes #

```
def node = session.rootNode.addNode('testMoveNode')
def node2 = session.rootNode.addNode('testMoveNode2')

node.move(node2.path)
assert session.getNode('/testMoveNode2/testMoveNode')
```