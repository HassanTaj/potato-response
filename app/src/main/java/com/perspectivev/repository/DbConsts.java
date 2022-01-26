package com.perspectivev.repository;

public class DbConsts {

    public static final String SIM_1 = "Sim 1";
    public static final String SIM_2 = "Sim 2";


    public static final String DB_NAME = "AutoResponderDB.db";
    public static final int DB_VERSION = 2;

    public static class AppServiceOptionsTbl{
        public static final String Tbl_Name ="AppServiceOptionsTbl";
        public static final String C_ID ="id";
        public static final String C_IsServiceActive ="isServiceAcrive";
        public static final String C_ResponseText ="responseText";
        public static final String C_SelectedSim ="selectedSim";
    }

    public static class ServiceLogTbl {
        public static final String Tbl_Name ="ServicesLogTbl";
        public static final String C_ID = "id";
        public static final String C_ServiceType = "serviceType";
        public static final String C_ServiceName = "serviceName";
        public static final String C_ServiceStartTime = "serviceStartTime";
        public static final String C_ServiceEndTime = "serviceEndTime";
    }

    public static class ServiceType {
        public static final String JOB_SERVICE = "job_service";
        public static final String BG_SERVICE = "background_service";
    }

}
