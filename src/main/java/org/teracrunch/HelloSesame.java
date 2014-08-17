package org.teracrunch;

import info.aduna.iteration.Iterations;

import java.io.File;
import java.io.IOException;

import org.openrdf.OpenRDFException;
import org.openrdf.model.Model;
import org.openrdf.model.Statement;
import org.openrdf.model.impl.LinkedHashModel;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.Rio;
import org.openrdf.sail.nativerdf.NativeStore;

public class HelloSesame {

	/**
	 * @param args
	 * @throws RepositoryException
	 */
	public static void main(String[] args) throws RepositoryException {
		File dataDir = new File("/data-dir");
		Repository repo = new SailRepository(new NativeStore(dataDir));
		repo.initialize();

		File file = new File("rdf-sources/kt-content.rdf.u8");
		String baseURI = "http://dmoz.org/rdf/";

		try {
			RepositoryConnection con = repo.getConnection();
			try {
				con.add(file, baseURI, RDFFormat.RDFXML);

				RepositoryResult<Statement> statements = con.getStatements(null, null, null, true);
				Model model = Iterations.addAll(statements, new LinkedHashModel());

				Rio.write(model, System.out, RDFFormat.TURTLE);

			} finally {
				con.close();
			}
		} catch (OpenRDFException e) {
			// handle exception
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
			// handle io exception
		}
	}

}
