package tests.models;

import br.com.mitra.actionJar.ActionContext;
import br.com.mitra.actionJar.CubeData;
import br.com.mitra.actionJar.DimensionData;
import br.com.mitra.actionJar.SourceData;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ActionExecutionTest {

    ActionContext actionContext;

    public ActionExecutionTest(){
        this.actionContext = new ActionContext() {

            private Connection connection = new TestConnection().getConnection();

            @Override
            public List<SourceData> getSourceContent() {
                List<SourceData> list = new ArrayList<>();
                    SourceData sourceData1 = new SourceData() {
                    @Override
                    public Object getField(String columnName) {
                        if(columnName.equals("debug")){
                            return "true";
                        }
                        if (columnName.equals("id")) {
                            return "titulosareceber";
                        }
                        if (columnName.equals("dataCadastroInicial")) {
                            return "2024-01-01";
                        }
                        if (columnName.equals("dataCadastroFinal")) {
                            return "2025-01-01";
                        }
                        if(columnName.equals("apiUrl")){
                            return "https://512499bimerapi.alterdata.cloud/api/titulosAReceber?limite={limite}&pagina={pagina}&dataCadastroInicial={dataCadastroInicial}&dataCadastroFinal={dataCadastroFinal}&identificadorPessoa={identificadorPessoa}&codigoEmpresa={codigoEmpresa}&dataVencimentoInicial={dataVencimentoInicial}&dataVencimentoFinal={dataVencimentoFinal}";
                        }
                        return null;
                    }
                };
                list.add(sourceData1);
                SourceData sourceData2 = new SourceData() {
                    @Override
                    public Object getField(String columnName) {
                        if(columnName.equals("debug")){
                            return "true";
                        }
                        if (columnName.equals("id")) {
                            return "planosdecontas";
                        }
                        if(columnName.equals("apiUrl")){
                            return "https://512499bimerapi.alterdata.cloud/api/naturezasLancamento";
                        }
                        return null;
                    }
                };
                list.add(sourceData2);

                SourceData sourceData3 = new SourceData() {
                    @Override
                    public Object getField(String columnName) {
                        if(columnName.equals("debug")){
                            return "true";
                        }
                        if (columnName.equals("id")) {
                            return "titulosapagar";
                        }
                        if (columnName.equals("dataCadastroInicial")) {
                            return "2024-01-01";
                        }
                        if (columnName.equals("dataCadastroFinal")) {
                            return "2025-01-01";
                        }
                        if(columnName.equals("apiUrl")){
                            return "https://512499bimerapi.alterdata.cloud/api/titulosAPagar?limite={limite}&pagina={pagina}&dataCadastroInicial={dataCadastroInicial}&dataCadastroFinal={dataCadastroFinal}&identificadorPessoa={identificadorPessoa}&codigoEmpresa={codigoEmpresa}&dataVencimentoInicial={dataVencimentoInicial}&dataVencimentoFinal={dataVencimentoFinal}";
                        }
                        return null;
                    }
                };
                list.add(sourceData3);

                SourceData sourceData4 = new SourceData() {
                    @Override
                    public Object getField(String columnName) {
                        if(columnName.equals("debug")){
                            return "true";
                        }
                        if (columnName.equals("id")) {
                            return "departamento";
                        }
                        if(columnName.equals("apiUrl")){
                            return "https://512499bimerapi.alterdata.cloud/api/centros-de-custo";
                        }
                        return null;
                    }
                };
                list.add(sourceData4);

                return list;
            }

            @Override
            public CubeData newCubeData(Integer cubeId) throws Exception {
                return null;
            }

            @Override
            public CubeData newCubeData(String cubeName) throws Exception {
                return null;
            }

            @Override
            public DimensionData newDimensionData(Integer dimensionId) throws Exception {
                return null;
            }

            @Override
            public DimensionData newDimensionData(String dimensionName) throws Exception {
                return null;
            }

            @Override
            public Connection getConnection() {
                return connection;
            }
        };
    }

    public ActionContext getActionContext() {
        return actionContext;
    }
}
