import javax.jcr.SimpleCredentials;
import javax.jcr.query.qom.QueryObjectModelConstants;

import org.apache.jackrabbit.core.TransientRepository;
import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.mnode.juicer.query.QueryBuilder;

def repoConfig = RepositoryConfig.create(QueryBuilderTest.getResource("/config.xml").toURI(),
	new File(System.getProperty("user.dir"), "repository").absolutePath)
def repository = new TransientRepository(repoConfig)

def session = repository.login(new SimpleCredentials('readonly', ''.toCharArray()))
Runtime.getRuntime().addShutdownHook({
	session.logout()
})

def attachments = new QueryBuilder(session.workspace.queryManager).with {
	query(
		source: selector(nodeType: 'nt:file', name: 'files'),
		constraint: and(
			constraint1: descendantNode(selectorName: 'files', path: '/'),
			constraint2: and(
				constraint1: not(comparison(
					operand1: nodeNamex(selectorName: 'files'),
					operator: QueryObjectModelConstants.JCR_OPERATOR_EQUAL_TO,
					operand2: literal(session.valueFactory.createValue('part')))),
				constraint2: not(comparison(
					operand1: nodeNamex(selectorName: 'files'),
					operator: QueryObjectModelConstants.JCR_OPERATOR_EQUAL_TO,
					operand2: literal(session.valueFactory.createValue('data'))))))
	)
}

println attachments.statement

def references = new QueryBuilder(session.workspace.queryManager).with {
	query(
		source: selector(nodeType: 'nt:unstructured', name: 'headers'),
		constraint: and(
			constraint1: descendantNode(selectorName: 'headers', path: '/messages'),
			constraint2: and(
				constraint1: comparison(
					operand1: nodeNamex(selectorName: 'headers'),
					operator: QueryObjectModelConstants.JCR_OPERATOR_EQUAL_TO,
					operand2: literal(session.valueFactory.createValue('headers'))),
				constraint2: or(
						constraint1: comparison(
								operand1: propertyValue(selectorName: 'headers', propertyName: 'In-Reply-To'),
								operator: QueryObjectModelConstants.JCR_OPERATOR_EQUAL_TO,
								operand2: bindVariable('messageId')
							),
						constraint2: comparison(
								operand1: propertyValue(selectorName: 'headers', propertyName: 'References'),
								operator: QueryObjectModelConstants.JCR_OPERATOR_EQUAL_TO,
								operand2: bindVariable('messageId')
							)
					)
				)
			)
	)
}

println references.statement
