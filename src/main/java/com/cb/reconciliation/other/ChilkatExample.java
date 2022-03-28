//package com.cb.reconciliation;
//
//import com.chilkatsoft.*;
//
//public class ChilkatExample {
//
//    static {
//        try {
//            System.loadLibrary("chilkat");
//        } catch (UnsatisfiedLinkError e) {
//            System.err.println("Native code library failed to load.\n" + e);
//            System.exit(1);
//        }
//    }
//
//    public static void main(String argv[])
//    {
//        // This example requires the Chilkat API to have been previously unlocked.
//        // See Global Unlock Sample for sample code.
//
//        CkHttp http = new CkHttp();
//
//        CkJsonObject jsonToken = new CkJsonObject();
//        boolean success = jsonToken.LoadFile("qa_data/tokens/xero-access-token.json");
//        if (success == false) {
//            System.out.println(jsonToken.lastErrorText());
//            return;
//        }
//
//        http.put_AuthToken(jsonToken.stringOf("access_token"));
//
//        // Replace the value here with an actual tenant ID obtained from this example:
//        // Get Xero Tenant IDs
//        http.SetRequestHeader("Xero-tenant-id","83299b9e-5747-4a14-a18a-a6c94f824eb7");
//
//        http.put_Accept("application/json");
//
//        CkHttpResponse resp = http.QuickRequest("GET","https://api.xero.com/api.xro/2.0/Invoices");
//        if (http.get_LastMethodSuccess() != true) {
//            System.out.println(http.lastErrorText());
//            return;
//        }
//
//        System.out.println("Response Status Code: " + resp.get_StatusCode());
//
//        CkJsonObject jsonResponse = new CkJsonObject();
//        jsonResponse.Load(resp.bodyStr());
//        jsonResponse.put_EmitCompact(false);
//        System.out.println(jsonResponse.emit());
//
//        if (resp.get_StatusCode() != 200) {
//            System.out.println("Failed.");
//
//            return;
//        }
//
//        // Sample output...
//        // (See the parsing code below..)
//        //
//        // Use the this online tool to generate parsing code from sample JSON:
//        // Generate Parsing Code from JSON
//
//        // {
//        //   "Id": "bda1f62f-0d63-4178-8d54-e91fd226987a",
//        //   "Status": "OK",
//        //   "ProviderName": "Chilkat2222",
//        //   "DateTimeUTC": "\/Date(1587210234493)\/",
//        //   "Invoices": [
//        //     {
//        //       "Type": "ACCPAY",
//        //       "InvoiceID": "0032f627-3156-4d30-9b1c-4d3b994dc921",
//        //       "InvoiceNumber": "9871",
//        //       "Reference": "",
//        //       "Payments": [
//        //         {
//        //           "PaymentID": "22974891-3689-4694-9ee7-fd2ba917af55",
//        //           "Date": "\/Date(1579737600000+0000)\/",
//        //           "Amount": 148.50,
//        //           "Reference": "Chq 409",
//        //           "HasAccount": false,
//        //           "HasValidationErrors": false
//        //         }
//        //       ],
//        //       "CreditNotes": [
//        //       ],
//        //       "Prepayments": [
//        //       ],
//        //       "Overpayments": [
//        //       ],
//        //       "AmountDue": 0.00,
//        //       "AmountPaid": 148.50,
//        //       "AmountCredited": 0.00,
//        //       "IsDiscounted": false,
//        //       "HasAttachments": false,
//        //       "HasErrors": false,
//        //       "Contact": {
//        //         "ContactID": "d6a384fb-f46f-41a3-8ac7-b7bc9e0b5efa",
//        //         "Name": "Melrose Parking",
//        //         "Addresses": [
//        //         ],
//        //         "Phones": [
//        //         ],
//        //         "ContactGroups": [
//        //         ],
//        //         "ContactPersons": [
//        //         ],
//        //         "HasValidationErrors": false
//        //       },
//        //       "DateString": "2020-01-15T00:00:00",
//        //       "Date": "\/Date(1579046400000+0000)\/",
//        //       "DueDateString": "2020-01-24T00:00:00",
//        //       "DueDate": "\/Date(1579824000000+0000)\/",
//        //       "Status": "PAID",
//        //       "LineAmountTypes": "Exclusive",
//        //       "LineItems": [
//        //       ],
//        //       "SubTotal": 135.00,
//        //       "TotalTax": 13.50,
//        //       "Total": 148.50,
//        //       "UpdatedDateUTC": "\/Date(1221560931500+0000)\/",
//        //       "CurrencyCode": "AUD",
//        //       "FullyPaidOnDate": "\/Date(1579737600000+0000)\/"
//        //     },
//        //     {
//        //       "Type": "ACCPAY",
//        //       "InvoiceID": "673dd7cc-beb7-4697-83d4-0c47cb400cc2",
//        //       "InvoiceNumber": "",
//        //       "Reference": "",
//        //       "Payments": [
//        //         {
//        //           "PaymentID": "4d06f609-5200-4364-9c8b-d4379a945252",
//        //           "Date": "\/Date(1580688000000+0000)\/",
//        //           "Amount": 974.60,
//        //           "Reference": "DD # 96013",
//        //           "HasAccount": false,
//        //           "HasValidationErrors": false
//        //         }
//        //       ],
//        //       "CreditNotes": [
//        //         {
//        //           "CreditNoteID": "7df8949c-b71f-40c0-bbcf-39f2f450f286",
//        //           "CreditNoteNumber": "03391",
//        //           "ID": "7df8949c-b71f-40c0-bbcf-39f2f450f286",
//        //           "HasErrors": false,
//        //           "AppliedAmount": 218.90,
//        //           "DateString": "2020-01-29T00:00:00",
//        //           "Date": "\/Date(1580256000000+0000)\/",
//        //           "LineItems": [
//        //           ],
//        //           "Total": 218.90
//        //         }
//        //       ],
//        //       "Prepayments": [
//        //       ],
//        //       "Overpayments": [
//        //       ],
//        //       "AmountDue": 0.00,
//        //       "AmountPaid": 974.60,
//        //       "AmountCredited": 218.90,
//        //       "IsDiscounted": false,
//        //       "HasAttachments": false,
//        //       "HasErrors": false,
//        //       "Contact": {
//        //         "ContactID": "d0cd2c4f-18a0-4f7c-a32a-2db00f29d298",
//        //         "Name": "PC Complete",
//        //         "Addresses": [
//        //         ],
//        //         "Phones": [
//        //         ],
//        //         "ContactGroups": [
//        //         ],
//        //         "ContactPersons": [
//        //         ],
//        //         "HasValidationErrors": false
//        //       },
//        //       "DateString": "2020-01-28T00:00:00",
//        //       "Date": "\/Date(1580169600000+0000)\/",
//        //       "DueDateString": "2020-02-04T00:00:00",
//        //       "DueDate": "\/Date(1580774400000+0000)\/",
//        //       "Status": "PAID",
//        //       "LineAmountTypes": "Exclusive",
//        //       "LineItems": [
//        //       ],
//        //       "SubTotal": 1085.00,
//        //       "TotalTax": 108.50,
//        //       "Total": 1193.50,
//        //       "UpdatedDateUTC": "\/Date(1221561913790+0000)\/",
//        //       "CurrencyCode": "AUD",
//        //       "FullyPaidOnDate": "\/Date(1580688000000+0000)\/"
//        //     },
//        //     {
//        //       "Type": "ACCPAY",
//        //       "InvoiceID": "c12aff7e-12bf-4185-8702-460929f19674",
//        //       "InvoiceNumber": "",
//        //       "Reference": "",
//        //       "Payments": [
//        //       ],
//        //       "CreditNotes": [
//        //       ],
//        //       "Prepayments": [
//        //       ],
//        //       "Overpayments": [
//        //       ],
//        //       "AmountDue": 2166.99,
//        //       "AmountPaid": 0.00,
//        //       "AmountCredited": 0.00,
//        //       "CurrencyRate": 1.000000,
//        //       "IsDiscounted": false,
//        //       "HasAttachments": false,
//        //       "HasErrors": false,
//        //       "Contact": {
//        //         "ContactID": "d0cd2c4f-18a0-4f7c-a32a-2db00f29d298",
//        //         "Name": "PC Complete",
//        //         "Addresses": [
//        //         ],
//        //         "Phones": [
//        //         ],
//        //         "ContactGroups": [
//        //         ],
//        //         "ContactPersons": [
//        //         ],
//        //         "HasValidationErrors": false
//        //       },
//        //       "DateString": "2020-04-10T00:00:00",
//        //       "Date": "\/Date(1586476800000+0000)\/",
//        //       "DueDateString": "2020-05-05T00:00:00",
//        //       "DueDate": "\/Date(1588636800000+0000)\/",
//        //       "Status": "AUTHORISED",
//        //       "LineAmountTypes": "Exclusive",
//        //       "LineItems": [
//        //       ],
//        //       "SubTotal": 1969.99,
//        //       "TotalTax": 197.00,
//        //       "Total": 2166.99,
//        //       "UpdatedDateUTC": "\/Date(1497965301980+0000)\/",
//        //       "CurrencyCode": "AUD"
//        //     }
//        //   ]
//        // }
//        //
//
//        String Type;
//        String InvoiceID;
//        String InvoiceNumber;
//        String Reference;
//        String AmountDue;
//        String AmountPaid;
//        String AmountCredited;
//        boolean IsDiscounted;
//        boolean HasAttachments;
//        boolean HasErrors;
//        String ContactContactID;
//        String ContactName;
//        boolean ContactHasValidationErrors;
//        String DateString;
//        String Date;
//        String DueDateString;
//        String DueDate;
//        String LineAmountTypes;
//        String SubTotal;
//        String TotalTax;
//        String Total;
//        String UpdatedDateUTC;
//        String CurrencyCode;
//        String FullyPaidOnDate;
//        String CurrencyRate;
//        int j;
//        int count_j;
//        String PaymentID;
//        String Amount;
//        boolean HasAccount;
//        boolean HasValidationErrors;
//        String CreditNoteID;
//        String CreditNoteNumber;
//        String ID;
//        String AppliedAmount;
//        int k;
//        int count_k;
//
//        String Id = jsonResponse.stringOf("Id");
//        String Status = jsonResponse.stringOf("Status");
//        String ProviderName = jsonResponse.stringOf("ProviderName");
//        String DateTimeUTC = jsonResponse.stringOf("DateTimeUTC");
//        int i = 0;
//        int count_i = jsonResponse.SizeOfArray("Invoices");
//        while (i < count_i) {
//            jsonResponse.put_I(i);
//            Type = jsonResponse.stringOf("Invoices[i].Type");
//            InvoiceID = jsonResponse.stringOf("Invoices[i].InvoiceID");
//            InvoiceNumber = jsonResponse.stringOf("Invoices[i].InvoiceNumber");
//            Reference = jsonResponse.stringOf("Invoices[i].Reference");
//            AmountDue = jsonResponse.stringOf("Invoices[i].AmountDue");
//            AmountPaid = jsonResponse.stringOf("Invoices[i].AmountPaid");
//            AmountCredited = jsonResponse.stringOf("Invoices[i].AmountCredited");
//            IsDiscounted = jsonResponse.BoolOf("Invoices[i].IsDiscounted");
//            HasAttachments = jsonResponse.BoolOf("Invoices[i].HasAttachments");
//            HasErrors = jsonResponse.BoolOf("Invoices[i].HasErrors");
//            ContactContactID = jsonResponse.stringOf("Invoices[i].Contact.ContactID");
//            ContactName = jsonResponse.stringOf("Invoices[i].Contact.Name");
//            ContactHasValidationErrors = jsonResponse.BoolOf("Invoices[i].Contact.HasValidationErrors");
//            DateString = jsonResponse.stringOf("Invoices[i].DateString");
//            Date = jsonResponse.stringOf("Invoices[i].Date");
//            DueDateString = jsonResponse.stringOf("Invoices[i].DueDateString");
//            DueDate = jsonResponse.stringOf("Invoices[i].DueDate");
//            Status = jsonResponse.stringOf("Invoices[i].Status");
//            LineAmountTypes = jsonResponse.stringOf("Invoices[i].LineAmountTypes");
//            SubTotal = jsonResponse.stringOf("Invoices[i].SubTotal");
//            TotalTax = jsonResponse.stringOf("Invoices[i].TotalTax");
//            Total = jsonResponse.stringOf("Invoices[i].Total");
//            UpdatedDateUTC = jsonResponse.stringOf("Invoices[i].UpdatedDateUTC");
//            CurrencyCode = jsonResponse.stringOf("Invoices[i].CurrencyCode");
//            FullyPaidOnDate = jsonResponse.stringOf("Invoices[i].FullyPaidOnDate");
//            CurrencyRate = jsonResponse.stringOf("Invoices[i].CurrencyRate");
//            j = 0;
//            count_j = jsonResponse.SizeOfArray("Invoices[i].Payments");
//            while (j < count_j) {
//                jsonResponse.put_J(j);
//                PaymentID = jsonResponse.stringOf("Invoices[i].Payments[j].PaymentID");
//                Date = jsonResponse.stringOf("Invoices[i].Payments[j].Date");
//                Amount = jsonResponse.stringOf("Invoices[i].Payments[j].Amount");
//                Reference = jsonResponse.stringOf("Invoices[i].Payments[j].Reference");
//                HasAccount = jsonResponse.BoolOf("Invoices[i].Payments[j].HasAccount");
//                HasValidationErrors = jsonResponse.BoolOf("Invoices[i].Payments[j].HasValidationErrors");
//                j = j+1;
//            }
//
//            j = 0;
//            count_j = jsonResponse.SizeOfArray("Invoices[i].CreditNotes");
//            while (j < count_j) {
//                jsonResponse.put_J(j);
//                CreditNoteID = jsonResponse.stringOf("Invoices[i].CreditNotes[j].CreditNoteID");
//                CreditNoteNumber = jsonResponse.stringOf("Invoices[i].CreditNotes[j].CreditNoteNumber");
//                ID = jsonResponse.stringOf("Invoices[i].CreditNotes[j].ID");
//                HasErrors = jsonResponse.BoolOf("Invoices[i].CreditNotes[j].HasErrors");
//                AppliedAmount = jsonResponse.stringOf("Invoices[i].CreditNotes[j].AppliedAmount");
//                DateString = jsonResponse.stringOf("Invoices[i].CreditNotes[j].DateString");
//                Date = jsonResponse.stringOf("Invoices[i].CreditNotes[j].Date");
//                Total = jsonResponse.stringOf("Invoices[i].CreditNotes[j].Total");
//                k = 0;
//                count_k = jsonResponse.SizeOfArray("Invoices[i].CreditNotes[j].LineItems");
//                while (k < count_k) {
//                    jsonResponse.put_K(k);
//                    k = k+1;
//                }
//
//                j = j+1;
//            }
//
//            j = 0;
//            count_j = jsonResponse.SizeOfArray("Invoices[i].Prepayments");
//            while (j < count_j) {
//                jsonResponse.put_J(j);
//                j = j+1;
//            }
//
//            j = 0;
//            count_j = jsonResponse.SizeOfArray("Invoices[i].Overpayments");
//            while (j < count_j) {
//                jsonResponse.put_J(j);
//                j = j+1;
//            }
//
//            j = 0;
//            count_j = jsonResponse.SizeOfArray("Invoices[i].Contact.Addresses");
//            while (j < count_j) {
//                jsonResponse.put_J(j);
//                j = j+1;
//            }
//
//            j = 0;
//            count_j = jsonResponse.SizeOfArray("Invoices[i].Contact.Phones");
//            while (j < count_j) {
//                jsonResponse.put_J(j);
//                j = j+1;
//            }
//
//            j = 0;
//            count_j = jsonResponse.SizeOfArray("Invoices[i].Contact.ContactGroups");
//            while (j < count_j) {
//                jsonResponse.put_J(j);
//                j = j+1;
//            }
//
//            j = 0;
//            count_j = jsonResponse.SizeOfArray("Invoices[i].Contact.ContactPersons");
//            while (j < count_j) {
//                jsonResponse.put_J(j);
//                j = j+1;
//            }
//
//            j = 0;
//            count_j = jsonResponse.SizeOfArray("Invoices[i].LineItems");
//            while (j < count_j) {
//                jsonResponse.put_J(j);
//                j = j+1;
//            }
//
//            i = i+1;
//        }
//    }
//}
