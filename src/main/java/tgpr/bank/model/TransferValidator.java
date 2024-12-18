package tgpr.bank.model;

import tgpr.bank.controller.EditTransferController;
import tgpr.framework.Controller;
import tgpr.framework.Error;
import tgpr.framework.ErrorList;
import tgpr.framework.Tools;

import java.text.DecimalFormat;
import java.util.List;
import java.util.regex.Pattern;

public abstract class TransferValidator   {


   public static Error isValidAmount(String am,Double floor,Double solde)
    {
        double res = -(floor)+solde;
        DecimalFormat df = new DecimalFormat("0.00");
        if (am.isBlank())
            return new Error("amount required",Transfer.Fields.amount);
        if (Tools.toDouble(am) > res )
            return new Error("amount must be < = "+ df.format(res)+" €",Transfer.Fields.amount);
        if (Tools.toDouble(am) <= 0 )
            return new Error("amount must be > 0 ",Transfer.Fields.amount);
        return Error.NOERROR;
    }
    public static Error isValidIban(String iban)
    {
        if (iban.isEmpty())
            return new Error("target iban required",Transfer.Fields.iban);
        if (!Pattern.matches("[A-Z]{2}[0-9]{2}[ ][0-9]{4}[ ][0-9]{4}[ ][0-9]{4}", iban))
            return new Error("wrong format iban",Transfer.Fields.iban);

        return Error.NOERROR;
    }
    public static Error isValidTitle(String title)
    {
        if (title == null)
            return new Error("target account title required",Transfer.Fields.title);
        if (!Pattern.matches("[A-Z]{3}", title))
            return new Error("invalid title", Transfer.Fields.title);
        return Error.NOERROR;
    }

    public static Error isValidDescription(String description)
    {
        if (description.isBlank())
            return new Error("Descption required",Transfer.Fields.description);

        return Error.NOERROR;
    }



}
