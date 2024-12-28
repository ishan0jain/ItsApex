export class UserGlobal {
    mdulAcs!: Array<String>;
    usrNm!: String;
    frstNm!: String;
    lstNm!: String;
    shopNm!: String;
    usrId!: Number;
    shopId!: Number;
    carierId!: Number;
    areaCd!: Number;
    ListOfModule = {
        "app-carier" : "Delivery Tasks",
        "app-buyer" : "Browse Products",
        "app-seller" : "Orders Placed",
        "app-register" : "Become a Seller/Carier",
        "app-about" : "About Us"
    }
}
