# Introduction #

Juicer provides some extensions to implementations of the javax.jcr.Session interface when used from within Groovy.


# withLock #

To support automatically locking access to a Session the withLock closure is added.

```
def lock = new ReentrantLock()

session.withLock(lock) {
  def node = rootNode.addNode('testLockedNode')
}
```