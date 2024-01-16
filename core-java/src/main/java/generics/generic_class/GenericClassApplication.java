package generics.generic_class;

import com.google.gson.Gson;
import lombok.ToString;

public class GenericClassApplication {
    public static void main(String[] args) {
        Processor<InquiryRequest, InquiryResponse> inquiryProcessor = new Processor<>();
        InquiryResponse inquiryResponse = inquiryProcessor.processRequest(new InquiryRequest(), InquiryResponse.class, new HttpClient<>());
        System.out.println(inquiryResponse);
        System.out.println();

        Processor<PaymentRequest, PaymentResponse> paymentProcessor = new Processor<>();
        PaymentResponse paymentResponse = paymentProcessor.processRequest(new PaymentRequest(), PaymentResponse.class, new HttpClient<>());
        System.out.println(paymentResponse);
        System.out.println();

        Processor<VoucherRequest, VoucherResponse> voucherProcessor = new Processor<>();
        VoucherResponse voucherResponse = voucherProcessor.processRequest(new VoucherRequest(), VoucherResponse.class, new HttpClient<>());
        System.out.println(voucherResponse);
    }
}

@ToString class Request {}

@ToString class InquiryRequest extends Request {}

@ToString class PaymentRequest extends Request {}

@ToString class VoucherRequest extends Request {}

@ToString class Response {}

@ToString class InquiryResponse extends Response {}

@ToString class PaymentResponse extends Response {}

@ToString class VoucherResponse extends Response {}


class Processor<T extends Request, U extends Response> {
    public U processRequest(T request , Class<U> responseType, HttpClient<T, U> httpClient){
        return httpClient.processRequest(request, responseType);
    }
}

class HttpClient<T extends Request, U extends Response> {
    public U processRequest(T request, Class<U> responseType) {
        System.out.println(String.format("processing %s", request));
        return new Gson().fromJson("{}", responseType);
    }
}