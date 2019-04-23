package model;

import java.util.ArrayList;
import java.util.List;

public class test {
    public static void main(String[] args) {
        System.out.println(checkerFunction.verifyDateFormat("2018-12-32"));
        List list = new ArrayList();
        SortingFunctions.AdminManageSiteAscOne(list);
        SortingFunctions.AdminManageSiteDescOne(list);
        SortingFunctions.AdminManageTransitColThree(list);
        SortingFunctions.ManagerManageStaffAscColThree(list);
        SortingFunctions.ManagerSiteReportAscColOne(list);
        SortingFunctions.AdminManageTransitOneDesc(list);
        SortingFunctions.ManagerManageStaffDescOne(list);
        SortingFunctions.ManagerSiteReportDescColOne(list);
        SortingFunctions.userTakeTransitAscColOne(list);
        SortingFunctions.StaffViewScheduleDesc(list);
        SortingFunctions.AdminManageSiteAscOne(list);
        SortingFunctions.userTakeTransitAscColFour(list);
        SortingFunctions.userTakeTransitDescColOne(list);
        SortingFunctions.userTakeTransitAscColtwo(list);
    }
}
