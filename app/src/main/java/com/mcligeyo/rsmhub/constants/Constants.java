package com.mcligeyo.rsmhub.constants;


import android.util.Base64;


public class Constants {

    //shared prefss
    public static final String PREF_NAME = "Pref";
    public static final String MAKE_REQ_PREF_NAME = "MakeRequisition";
    public static final String CREATE_REQUISITION_PREF_NAME = "";
    public static final String REQUISITION_ITEM_PREF_NAME = "Requisition Item";
    public static final String REQUISITION_ITEM_VIEW_GET_REQ_DATA_VIEW_GET = "Requisition Item View Get Req Data View Get";
    public static final String EDITED_REQUISITION_ITEM_PREF_NAME = "Edited Requisition Item";
    public static final String GET_REQUISITION_ITEMS_PREF_NAME = "Get Requisition Items";
    public static final String SUBMITED_REQUISITION_ITEM_PREF_NAME = "submited Req items";
    public static final String GET_REQ_ITEMS_BELONGING_TO_REQUISITION = "Get Req Items Belonging To Requisition";
    public static final String GET_REQ_ID_REQ_ITEMS = "Get Req Id Req Items";
    public static final String USER_VIEW_BY_ID = "viewbyIdUser";
    public static final String REQUISITION_ITEM_WITH_REQUISITION = "requisitionItemRequisitionWith";
    public static final String PENDING_STATUS = "pending status";
    public static final String REQITEMWITHREQ = "REQITEMWITHreq";
    public static final String REQPREF_REQITPREF = "REQPREF_REQITPREF";
    public static final String REQITPREF_REQPREF = "REQITPREF_REQPREF";


    public static final int REQUEST_CODE = 2;
    public static final int REQUEST_CODE1 = 3;
    public static final int REQUEST_CODE2 = 4;


    public static final String AUTH = "Basic " + Base64.encodeToString(("admin@admin.com:password").getBytes(), Base64.NO_WRAP);
    public static final String BASE_URL = "http://192.168.8.171:5050/rsmhubadmin/public/api/";   //"https://ken.roadshowmasters.com/api/"
    //public static final String BASE_URL2 = "http://192.168.0.111/rsmhubadmin/public/api/";   //"https://ken.roadshowmasters.com/api/"; faiba
    public static final String BASE_URL2 = "https://myrsmhub.roadshowmasters.com/api/"; //"https://ken.roadshowmasters.com/api/"

    public static final String LOG_IN_URL = BASE_URL + "login";
    public static final String MAKE_REQUISITION_URL = BASE_URL + "requisition/create";
    public static final String MAKE_REQUISITION_ITEM_URL = BASE_URL + "requisitionitem/create";

    //
    public static final String GET_REQUISITION_ITEM_URL = BASE_URL + "requisitionitem/view";
    public static final String PUT_REQUISITION_ITEM_URL = BASE_URL + "requisitionitem/edit";
    public static final String GET_REQUISITION_URL = BASE_URL + "requisition/view";

    //
    public static final String DELETE_REQUISITION_ITEM_URL = BASE_URL + "requisitionitem/delete";
    public static final String SUBMIT_REQUISITION_ITEM_URL = BASE_URL + "requisition/submit";
    public static final String VIEW_USER_BY_ID = BASE_URL + "user/view";

    //FAIBA
    public static final String LOG_IN_URL2 = BASE_URL2 + "login";
    public static final String MAKE_REQUISITION_URL2 = BASE_URL2 + "requisition/create";
    public static final String MAKE_REQUISITION_ITEM_URL2 = BASE_URL2 + "requisitionitem/create";

    //
    public static final String GET_REQUISITION_ITEM_URL2 = BASE_URL2 + "requisitionitem/view";
    public static final String PUT_REQUISITION_ITEM_URL2 = BASE_URL2 + "requisitionitem/edit";
    public static final String GET_REQUISITION_URL_2 = BASE_URL2 + "requisition/view";
    public static final String GET_REQUISITION_ITEM_WITH_REQUISITION_URL_2 = BASE_URL2 + "requisitionitem/view";

    //
    public static final String DELETE_REQUISITION_ITEM_URL2 = BASE_URL2 + "requisitionitem/delete";
    public static final String SUBMIT_REQUISITION_ITEM_URL2 = BASE_URL2 + "requisition/submit";
    public static final String VIEW_USER_BY_ID2 = BASE_URL2 + "user/view";


    //savedinstancestate constants for reqdetailsact
    public static final String BUNDLE_RECYCLER_LAYOUT = "recycler_layout"; //used to mantain scroll position in recyclerview wen phone turned
    public static final String LIST_STATE = "list_state";

}

