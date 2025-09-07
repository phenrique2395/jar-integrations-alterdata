package tests;

import hnz.mitra.siteware.omie.ExecuteRequest;
import tests.models.ActionExecutionTest;

public class ExecuteQueryTest {
    public static void main(String[] args) {
        ExecuteRequest executeQuery = new ExecuteRequest();
        System.out.println("Iniciando execução de teste");
        executeQuery.doExecute(new ActionExecutionTest().getActionContext());
        System.out.println("Finalizando execução de teste");
    }
}
