package hnz.mitra.siteware.omie;

import br.com.mitra.actionJar.ActionContext;
import br.com.mitra.util.MitraDevTools;
import hnz.mitra.siteware.omie.models.apiSources.ImportDataSource;
import hnz.mitra.siteware.omie.service.APISourceService;
import hnz.mitra.siteware.omie.service.DdlService;
import hnz.mitra.siteware.omie.utils.PrintUtils;

import java.io.IOException;
import java.sql.SQLException;
import java.text.ParseException;

public class ExecuteRequest implements MitraDevTools{

	@Override
	public void doExecute(ActionContext actionContext) {
		Long executionStart = System.currentTimeMillis();
		ImportDataSource importDataSource = new ImportDataSource(null);
		PrintUtils.setDescription("Execute request");
		PrintUtils.setDebug(actionContext);
		PrintUtils.print("Starting execution.");
		APISourceService APISourceService = new APISourceService(actionContext);
		try {
			APISourceService.importData(importDataSource);
		} catch (SQLException | ParseException | IOException | InterruptedException e) {
			e.printStackTrace();
			PrintUtils.printError("Finished import execution with error: "+e.getMessage());
			throw new RuntimeException(e);
		}finally {
			try {
				PrintUtils.printDebug("Closing connection.");
				actionContext.getConnection().close();
				PrintUtils.printDebug("Connection closed.");
			} catch (SQLException e) {
				e.printStackTrace();
				throw new RuntimeException(e);
			}
		}
		PrintUtils.duration("Finished query import execution.",executionStart);
	}
}
