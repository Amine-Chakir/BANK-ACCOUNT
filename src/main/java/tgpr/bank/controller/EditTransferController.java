package tgpr.bank.controller;

import com.googlecode.lanterna.gui2.Window;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import tgpr.bank.model.*;
import tgpr.bank.view.EditTransferView;
import tgpr.framework.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class EditTransferController extends Controller {
    private final EditTransferView view;
    private Transfer transfer;
    private Favourite favourite;

    private Account account;

    private TransferCategory tc;
    private final boolean isNew;



    public EditTransferController() {
        this(null,null,null);
    }
    public EditTransferController(Transfer transfer,Account account,Favourite favourite) {
       this.account = account;
        this.transfer = transfer;
        this.favourite = favourite;
        isNew = transfer == null;
        view = new EditTransferView(this, transfer,account,favourite);
    }

    public List<Category> getAll(){return Category.getAll();}
   public List<Account> getgetAllSaufTargetAccount(String email,String iban){return Account.getAllSaufTargetAccount(email, iban);}
    public Transfer addTransfer() {
        var controller = new EditTransferController();
        navigateTo(controller);
        return controller.getTransfer();
    }
    public List<Account> getFavAcc(String email) {
        return Favourite.getFavAcc(email);
    }
    @Override
    public Window getView() {
        return view;
    }

    public Account getAccount() {
        return account;
    }

    public Transfer getTransfer() {
        return transfer;
    }
    public void save(double amount, String description, int source_account, int target_account, double source_saldo, double targer_saldo, LocalDateTime created_at, int created_by, LocalDate effective_at, String state) {
        account = Account.getIbanForAccountAbyid(target_account);
        var errors = validate(account.getIban(),description,Tools.toString(source_saldo), account.getTitle(), account.getFloor(),source_saldo);
      if (errors.isEmpty()){
          transfer = new Transfer(amount,description,source_account,target_account,source_saldo,targer_saldo,created_at,created_by,effective_at,state);
          transfer.save();
       //   view.close();
      }else
          showErrors(errors);

    }
    public void Info(){
        ViewManager.showMessage("Transfer done","information transfert");
        view.close();
    }
    public void updateSolde(int id, Double solde){ account.Update(id,solde); }

    public void addTransferCategory(int idcat,int idTra,int idAcc)
    {
        tc = new TransferCategory(idcat,idTra,idAcc);
        tc.add(idcat, idTra, idAcc);
    }

    public void addFavAccount(int iduser,int idaccount){

        favourite = new Favourite(idaccount,iduser);
        favourite.save(iduser, idaccount);

    }
    public Category getIdbyName(String name){
       return Category.getIdbyName(name);
    }

    public User getUserbyMail(){

        return User.getByEmail("bob@test.com");
    }
    public Transfer getIdLast(){
        return Transfer.LastIdTransfer();
    }

    public ErrorList validate(String iban,String description,String amount,String title,Double floor, Double solde)

    {
        var errors = new ErrorList();
            errors.add(TransferValidator.isValidIban(iban));
            errors.add(TransferValidator.isValidTitle(title));
            errors.add(TransferValidator.isValidAmount(amount,floor,solde));
            errors.add(TransferValidator.isValidDescription(description));
        return errors;
    }

}
