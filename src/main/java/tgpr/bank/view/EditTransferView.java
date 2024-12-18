package tgpr.bank.view;

import com.googlecode.lanterna.TerminalPosition;
import com.googlecode.lanterna.TerminalSize;
import com.googlecode.lanterna.TextColor;
import com.googlecode.lanterna.gui2.*;
import com.googlecode.lanterna.gui2.Button;
import com.googlecode.lanterna.gui2.GridLayout;
import com.googlecode.lanterna.gui2.Label;
import com.googlecode.lanterna.gui2.LayoutManager;
import com.googlecode.lanterna.gui2.Panel;
import com.googlecode.lanterna.gui2.dialogs.DialogWindow;
import com.googlecode.lanterna.gui2.dialogs.MessageDialogButton;
import com.googlecode.lanterna.input.KeyStroke;
import tgpr.bank.model.*;
import tgpr.framework.*;
import tgpr.bank.controller.EditTransferController;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;


public class EditTransferView extends DialogWindow implements ComboBox.Listener {
    private Button btnAdd;
    private Label errTitle;
    private Label errIban;
    private Label errAmount;
    private Label errDesc;
    private Account account1;
    private Account account2;
    private User user;

    private final ObjectTable<Transfer> table = null;
    private final EditTransferController controller;
    private TextBox txtIban;
    private TextBox txtTitle = new  TextBox();
    private final TextBox txtAmount;
    private final TextBox txtDescription;
    private final TextBox txtEffectDate;
    private final CheckBox chkAddTofav = new CheckBox();
    private final ComboBox cbxSource;
    private ComboBox cbxTarget ;
    private  Transfer transfer;
    private  Category category;
   // private final Account account;
   private List<Account> l ;
    private List<Account> k = null;
    private final ComboBox cbxCategory;
    String email="bob@test.com";


    public enum state{
        executed,future,ignored,rejected
    }

    public EditTransferView(EditTransferController controller, Transfer transfer,Account account,Favourite fav) {

        // définit le titre de la fenêtre
        super((transfer == null ? "Create " : "Update ") + "Transfer");

        this.transfer = transfer;
        this.controller = controller;
      //  this.account = account;

        setHints(List.of(Hint.CENTERED, Hint.FIXED_SIZE));
        // permet de fermer la fenêtre en pressant la touche Esc
        setCloseWindowWithEscape(true);
        // définit une taille fixe pour la fenêtre de 15 lignes et 70 colonnes
        setFixedSize(new TerminalSize(100, 30));

        Panel root = new Panel();
        root.setLayoutManager(new GridLayout(2).setTopMarginSize(1));
        setComponent(root);
         //Favourite.getFavAcc("bob@test.com")
        new Label("Source Account:").addTo(root);
        cbxSource = new ComboBox<>(Account.getAll(email)).addTo(root);
        new EmptySpace().addTo(root);
        new EmptySpace().addTo(root);
        new Label("Target Account:").addTo(root);
        account1 = (Account) cbxSource.getSelectedItem();
        cbxTarget = new ComboBox<>("-- Encode IBAN myself--",Account.getAllSaufTargetAccount(email,account1.getIban())).addTo(root);
        l = Account.getFavAcc(email);
        for (Account elem : l)
        {
            cbxTarget.addItem(elem.getIban()+"| "+elem.getTitle()+" | Favourite");

        }
        // cbxTarget.addItem(Favourite.getFavAcc(email)).addTo(root);
        cbxSource.addListener((oldIndex, newIndex, byUser) -> {
            cbxTarget.clearItems();

            account1 = (Account) cbxSource.getSelectedItem();

           k = Account.getAllSaufTargetAccount(email,account1.getIban());
           for (Account elem : k)
           {
               cbxTarget.addItem(elem);
           }
            l = Account.getFavAcc(email);
            for (Account elem : l)
            {
                cbxTarget.addItem(elem.getIban()+"| "+elem.getTitle()+" | favourite");
            }

        }
        );

        new EmptySpace().addTo(root);
        new EmptySpace().addTo(root);
        new Label("Iban:").addTo(root);
        txtIban = new TextBox(new TerminalSize(21, 1)).addTo(root)
                .setTextChangeListener((txt, byUser) -> validate());
        new EmptySpace().addTo(root);
       errIban = new Label("").setForegroundColor(TextColor.ANSI.RED).addTo(root);
        new EmptySpace().addTo(root);
        new EmptySpace().addTo(root);
        cbxTarget.addListener((oldIndex, newIndex, byUser)->
                {
                    try
                    {    account2 = (Account) cbxTarget.getSelectedItem();
                        txtIban.setEnabled(false).setText(account2.getIban());
                        txtTitle.setEnabled(false).setText(account2.getTitle());
                    }
                    catch (Exception e)
                    {
                        txtIban.setText(cbxTarget.getSelectedItem().toString().substring(0,19));
                        txtTitle.setText(cbxTarget.getSelectedItem().toString().substring(21,24));
                    }


                }
                );

        new EmptySpace().addTo(root);
        new EmptySpace().addTo(root);
        new Label("Title:").addTo(root);
        txtTitle= new TextBox(new TerminalSize(21, 1)).addTo(root)
                .setTextChangeListener((txt, byUser) -> validate());
        new EmptySpace().addTo(root);
         errTitle = new Label("").setForegroundColor(TextColor.ANSI.RED).setForegroundColor(TextColor.ANSI.RED).addTo(root);
        new EmptySpace().addTo(root);
        new EmptySpace().addTo(root);
        new Label("add to favourites").addTo(root);
        chkAddTofav.addTo(root);
        new EmptySpace().addTo(root);
        new EmptySpace().addTo(root);
        new Label("amount:").addTo(root);
        txtAmount = new TextBox(new TerminalSize(11, 1)).setTextChangeListener((txt, byUser) -> validate()).addTo(root);
        new EmptySpace().addTo(root);
         errAmount = new Label("").setForegroundColor(TextColor.ANSI.RED).addTo(root);
        new EmptySpace().addTo(root);
        new EmptySpace().addTo(root);
        new Label("Descritpion:").addTo(root);
        txtDescription = new TextBox(new TerminalSize(20, 3)).setTextChangeListener((txt, byUser) -> validate()).addTo(root);
        new EmptySpace().addTo(root);
        errDesc = new Label("").setForegroundColor(TextColor.ANSI.RED).addTo(root);
        new EmptySpace().addTo(root);
        new EmptySpace().addTo(root);
        new Label("Effect Date:").addTo(root);
        txtEffectDate = new TextBox(new TerminalSize(11, 1)).addTo(root);
        new EmptySpace().addTo(root);
        new EmptySpace().addTo(root);
        new Label("Category:").addTo(root);
        cbxCategory = new ComboBox<>("no Category",Category.getAll()).addTo(root);
        new EmptySpace().addTo(root);
        new EmptySpace().addTo(root);
        var buttons = new Panel().addTo(root).setLayoutManager(new LinearLayout(Direction.HORIZONTAL));
        btnAdd =  new Button("add", this::add).setEnabled(false).addTo(buttons);

        new Button("Close", this::close).addTo(buttons);



    }

    /*
private Panel createcentredfields()
{

    Panel panel = new Panel();
    new Label("Iban:").addTo(panel);
    txtIban.setSize(new TerminalSize(20, 1)).addTo(panel);
    new EmptySpace().addTo(panel);
    new EmptySpace().addTo(panel);
    new Label("Title:").addTo(panel);
    txtTitle.setSize(new TerminalSize(11, 1)).addTo(panel);
    new EmptySpace().addTo(panel);
    new EmptySpace().addTo(panel);
    new Label("add to favourites").addTo(panel);
    chkAddTofav.addTo(panel);
    new EmptySpace().addTo(panel);
    new EmptySpace().addTo(panel);

    return panel;
}*/



   private void add() {
       LocalDateTime today = LocalDateTime.now();
       LocalDate today1 = LocalDate.now();
       Account account,account2;
       LocalDate effectdate;
       account=Account.getIdForAccountAbyIban(account1.getIban());
       account2= Account.getIdForAccountAbyIban(txtIban.getText());
       user = controller.getUserbyMail();
       if(txtEffectDate.getText()=="")
            effectdate = today1;
       else
           effectdate = Tools.toDate(txtEffectDate.getText());
       controller.save(
              Tools.toDouble(txtAmount.getText()),
               txtDescription.getText(),
               account.getId(),
               account2.getId(),
               account1.getSaldo(),
               account2.getSaldo(),
               today,
               user.getId(),
               effectdate,
               state(today1,effectdate)

        );
       //mise à jour des soldes après transfer
            try {
                controller.updateSolde(account1.getId(),account1.getSaldo()-Tools.toDouble(txtAmount.getText()));
                controller.updateSolde(account2.getId(),account2.getSaldo()+Tools.toDouble(txtAmount.getText()));
            }
            catch (Exception e)
            {
                throw new RuntimeException("erreur au niveau du màj du solde");

            }


       // ajout favoris account
       try {
           if (chkAddTofav.isChecked())
           {
               user = User.getByEmail(email);
               controller.addFavAccount(user.getId(), account2.getId());
           }
       }
       catch (Exception e)
       {
           throw new RuntimeException("ajout impossible pour favoris");
       }
       // TO DO  insert dans la table Transfer_Category
      if (cbxCategory.getSelectedItem()!= null)
       {
           transfer = controller.getIdLast();
           category =  controller.getIdbyName(cbxCategory.getSelectedItem().toString());
           controller.addTransferCategory(category.getId(), transfer.getId(), account1.getId());
           System.out.println(category.getId());
           System.out.println(transfer.getId());
           System.out.println(account1.getId());
       }
       controller.Info();


   }


    private String state(LocalDate now,LocalDate effec)
    {
        var res = "ignored";
        if (effec == null)
       effec = LocalDate.now();
        if (effec.isAfter(now))
           res = state.future.toString();
        else if (now.isEqual(effec))
            res = state.executed.toString();

        return res;

    }

    private void validate() {

       var errors = controller.validate(txtIban.getText(),txtDescription.getText(),txtAmount.getText(),txtTitle.getText(), account1.getFloor(),account1.getSaldo());
         errIban.setText(errors.getFirstErrorMessage(Transfer.Fields.iban));
        errTitle.setText(errors.getFirstErrorMessage(Transfer.Fields.title));
         errAmount.setText(errors.getFirstErrorMessage(Transfer.Fields.amount));
         errDesc.setText(errors.getFirstErrorMessage(Transfer.Fields.description));


         btnAdd.setEnabled(errors.isEmpty());


    }
    public void refresh()
    {
      /*  if (account!=cbxSource.getSelectedItem())
        {
          ;

        }*/

    }

    public void reloadData() {
     /*   if (this.account!=null)
        {

        cbxTarget.clearItems();
        account = (Account) cbxSource.getSelectedItem();
        // ajoute l'ensemble des membres au tableau
        cbxSource.addItem(Account.getAllSaufTargetAccount(email,account.getIban()));}*/
    }




    @Override
    public void onSelectionChanged(int i, int i1, boolean b) {

    }
}
