import { Cart } from "./cart";

export class Order extends Cart{
    status! : String;
    paymentStatus! : String;
    carierId! : String;
}
