/*    */ package com.teamcenter.rac.ui.commands.newbo;
/*    */ 
/*    */ import com.teamcenter.rac.aif.kernel.InterfaceAIFComponent;
/*    */ import com.teamcenter.rac.ui.commands.create.bo.NewBOModel;
/*    */ import com.teamcenter.rac.ui.commands.create.bo.NewBOWizard;
/*    */ import com.teamcenter.rac.util.AbstractCustomPanel;
/*    */ import com.teamcenter.rac.util.IPageComplete;
/*    */ import org.eclipse.swt.layout.GridData;
/*    */ import org.eclipse.swt.layout.GridLayout;
/*    */ import org.eclipse.swt.widgets.Composite;
/*    */ import org.eclipse.swt.widgets.Label;
/*    */ import org.eclipse.swt.widgets.Text;
/*    */ import org.eclipse.ui.forms.widgets.FormToolkit;
/*    */ 
/*    */ public class MyPanel extends AbstractCustomPanel
/*    */   implements IPageComplete
/*    */ {
/*    */   private Composite composite;
/*    */   private Text text;
/*    */ 
/*    */   public MyPanel()
/*    */   {
/*    */   }
/*    */ 
/*    */   public MyPanel(Composite parent)
/*    */   {
/* 24 */     super(parent);
/*    */   }
/*    */ 
/*    */   public void createPanel()
/*    */   {
/* 30 */     FormToolkit toolkit = new FormToolkit(this.parent.getDisplay());
/* 31 */     this.composite = toolkit.createComposite(this.parent);
/*    */ 
/* 33 */     GridLayout gl = new GridLayout(2, false);
/* 34 */     this.composite.setLayout(gl);
/*    */ 
/* 36 */     GridData gd = new GridData(768);
/* 37 */     gd.grabExcessHorizontalSpace = true;
/*    */ 
/* 39 */     this.composite.setLayoutData(gd);
/*    */ 
/* 41 */     GridData labelGD = new GridData(128);
/* 42 */     Label label = toolkit.createLabel(this.composite, "Object_Name: ");
/* 43 */     label.setLayoutData(labelGD);
/*    */ 
/* 45 */     GridData typeTextGd = new GridData(768);
/* 46 */     this.text = toolkit.createText(this.composite, "");
/* 47 */     this.text.setText("This is my own panel");
/* 48 */     this.text.setLayoutData(typeTextGd);
/*    */   }
/*    */ 
/*    */   public boolean isPageComplete()
/*    */   {
/* 53 */     String txt = this.text.getText();
/* 54 */     return txt.length() != 0;
/*    */   }
/*    */ 
/*    */   public Composite getComposite()
/*    */   {
/* 60 */     return this.composite;
/*    */   }
/*    */ 
/*    */   public void updatePanel()
/*    */   {
/* 66 */     if (this.input != null)
/*    */     {
/* 68 */       NewBOWizard wizard = (NewBOWizard)this.input;
/* 69 */       String msg = "";
/* 70 */       if (wizard.model.getTargetArray() != null)
/*    */       {
/*    */         try
/*    */         {
/* 74 */           msg = wizard.model.getTargetArray()[0].getProperty(
/* 75 111*/             "object_name").toString();
/*    */         }
/*    */         catch (Exception e)
/*    */         {
/* 79 */           e.printStackTrace();
/*    */         }
/*    */       }
/*    */       else
/*    */       {
/* 84 */         msg = "Nothing is selected";
/*    */       }
/* 86 */       this.text.setText(msg);
/*    */     }
/*    */   }
/*    */ 
/*    */   public Object getUserInput()
/*    */   {
/* 93 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Users\Jerry\Desktop\
 * Qualified Name:     com.teamcenter.rac.ui.commands.newbo.MyPanel
 * JD-Core Version:    0.6.2
 */