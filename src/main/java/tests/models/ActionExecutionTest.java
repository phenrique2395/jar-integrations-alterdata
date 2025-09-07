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
                        if(columnName.equals("id")){
                            return "table_test";
                        }
                        if(columnName.equals("apiUrl")){
                            return "https://api.exactspotter.com/v3/LeadsAndPersons?$skip={0,number,#}&$filter=stage eq ''Agendados''";
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
                        if(columnName.equals("id")){
                            return "table_test_agendamentos";
                        }
                        if(columnName.equals("apiUrl")){
                            return "https://api.exactspotter.com/v3/meetings?$skip={0,number,#}&$filter=type eq ''Vigente''";
                        }
                        return null;
                    }
                };
                list.add(sourceData2);
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
