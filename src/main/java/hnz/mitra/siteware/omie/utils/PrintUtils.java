package hnz.mitra.siteware.omie.utils;

import br.com.mitra.actionJar.ActionContext;
import hnz.mitra.siteware.omie.models.APISource;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class PrintUtils {

    static private APISource apiSource;
    private static DateTimeFormatter defaultDateFormat = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
    private static String description;
    private static String system;
    public static String defaultPrintTitle ="<Thread "+Thread.currentThread().getName()+"> Alter Data Integration Step JAR - "+ (!Objects.isNull(system)?"System "+system:"")+" : ";
    public static Boolean debug;

    public static void setApiSource(APISource apiSource) {
        PrintUtils.apiSource = apiSource;
        PrintUtils.description = apiSource.getDescription();
    }

    public static void setDebug(ActionContext actionContext) {
        if(actionContext==null||actionContext.getSourceContent().isEmpty()) {
            PrintUtils.debug=Boolean.FALSE;
            return;
        }
        String debugString = (String) actionContext.getSourceContent().stream().findFirst().get().getField("debug");
        PrintUtils.debug = debugString==null?Boolean.FALSE:debugString.equals("true")?Boolean.TRUE:Boolean.FALSE;
    }

    public static void setDescription(String description) {
        PrintUtils.description = description;
    }

    public static void setSystem(String system) {
        PrintUtils.system = system;
    }

    public static void print(String information){
        System.out.println(LocalDateTime.now().format(defaultDateFormat)+" [INFO] "+defaultPrintTitle+description+": "+information);
    }

    public static void duration(String information, Long executionStart){
        System.out.println(LocalDateTime.now().format(defaultDateFormat)+" [INFO] "+defaultPrintTitle+description+": "+information+" Duration: "+String.format("%.3f",Double.parseDouble(String.valueOf(System.currentTimeMillis()-executionStart))/1000)+"s");
    }

    public static void printDebug(String information){
        if(PrintUtils.debug) System.out.println(LocalDateTime.now().format(defaultDateFormat)+" [DEBUG] "+defaultPrintTitle+description+": "+information);
    }

    public static void durationDebug(String information, Long executionStart){
        if(PrintUtils.debug) System.out.println(LocalDateTime.now().format(defaultDateFormat)+" [DEBUG] "+defaultPrintTitle+description+": "+information+". Duration: "+String.format("%.3f",Double.parseDouble(String.valueOf(System.currentTimeMillis()-executionStart))/1000)+"s");
    }

    public static void printError(String information){
        System.out.println(LocalDateTime.now().format(defaultDateFormat)+" [ERROR] "+defaultPrintTitle+description+": "+information);
    }

    public static void printWarning(String information){
        System.out.println(LocalDateTime.now().format(defaultDateFormat)+" [WARNING] "+defaultPrintTitle+description+": "+information);
    }
}
