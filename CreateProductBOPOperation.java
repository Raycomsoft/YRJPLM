package com.teamcenter.rac.cme.ebop.newproductbop;

import com.teamcenter.rac.cme.bvr.connect.bopcontext.AbstractCreateBOPContextOperation;
import com.teamcenter.rac.cme.bvr.create.base.ILicenseChecker;
import com.teamcenter.rac.cme.bvr.create.fromtemplate.METemplateInfoPanel;
import com.teamcenter.rac.cme.kernel.bvr.TCComponentMEGenericBOP;
import com.teamcenter.rac.commands.newitem.NewItemDialog;

public class CreateProductBOPOperation extends AbstractCreateBOPContextOperation
{
  public CreateProductBOPOperation(NewItemDialog paramNewItemDialog, ILicenseChecker paramILicenseChecker)
  {
    super(paramNewItemDialog, paramILicenseChecker);
  }

  protected String getCloningRule()
  {
    if ((this.templateInfoPanel.getTemplateItem() instanceof TCComponentMEGenericBOP))
      return "GenericBOPTemplate";
    return "ProductBOPTemplate";
  }
}

/* Location:           C:\Siemens\Teamcenter11\portal\plugins\com.teamcenter.rac.cme.ebop.module_11000.2.0.jar
 * Qualified Name:     com.teamcenter.rac.cme.ebop.newproductbop.CreateProductBOPOperation
 * JD-Core Version:    0.6.2
 */