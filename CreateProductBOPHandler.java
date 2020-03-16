package com.teamcenter.rac.cme.ebop.newproductbop;

import com.teamcenter.rac.cme.bvr.connect.bopcontext.AbstractCreateBOPContextHandler;
import com.teamcenter.rac.cme.bvr.connect.bopcontext.AbstractCreateBOPContextHandler.AbstractBOPContextOperationCreator;
import com.teamcenter.rac.cme.bvr.create.base.ICreateItemOperationCreator;
import com.teamcenter.rac.cme.bvr.create.base.ILicenseChecker;
import com.teamcenter.rac.commands.newitem.NewItemDialog;
import com.teamcenter.rac.commands.newitem.NewItemOperation;
import com.teamcenter.rac.commands.newitem.NewItemPanel;
import com.teamcenter.rac.kernel.TCSession;
import java.awt.Frame;

public class CreateProductBOPHandler extends AbstractCreateBOPContextHandler
{
  protected String getObjectType()
  {
    return "MEProductBOP";
  }

  protected ICreateItemOperationCreator getOperationCreator()
  {
    return new EBOPOperationCreator(getLicenseChecker());
  }

  protected NewItemPanel createPanel(Frame paramFrame, TCSession paramTCSession, NewItemDialog paramNewItemDialog, ICreateItemOperationCreator paramICreateItemOperationCreator)
  {
    return new CreateProductBOPPanel(paramFrame, paramTCSession, paramNewItemDialog, paramICreateItemOperationCreator);
  }

  private class EBOPOperationCreator extends AbstractCreateBOPContextHandler.AbstractBOPContextOperationCreator
  {
    public EBOPOperationCreator(ILicenseChecker arg2)
    {
      super(localILicenseChecker);
    }

    protected NewItemOperation createOperation(NewItemDialog paramNewItemDialog, ILicenseChecker paramILicenseChecker)
    {
      this.operation = new CreateProductBOPOperation(paramNewItemDialog, CreateProductBOPHandler.this.getLicenseChecker());
      return this.operation;
    }
  }
}

/* Location:           C:\Siemens\Teamcenter11\portal\plugins\com.teamcenter.rac.cme.ebop.module_11000.2.0.jar
 * Qualified Name:     com.teamcenter.rac.cme.ebop.newproductbop.CreateProductBOPHandler
 * JD-Core Version:    0.6.2
 */