package com.teamcenter.rac.cme.ebop.newproductbop;

import com.teamcenter.rac.cme.bvr.connect.bopcontext.AbstractCreateBOPContextPanel;
import com.teamcenter.rac.cme.bvr.create.base.ICreateItemOperationCreator;
import com.teamcenter.rac.cme.ebop.newbase.LDFormPanelManager;
import com.teamcenter.rac.cme.ebop.newbase.PartitionTypePanelManager;
import com.teamcenter.rac.commands.newitem.NewItemDialog;
import com.teamcenter.rac.kernel.TCSession;
import java.awt.Frame;

public class CreateProductBOPPanel extends AbstractCreateBOPContextPanel
{
  public CreateProductBOPPanel(Frame paramFrame, TCSession paramTCSession, NewItemDialog paramNewItemDialog, ICreateItemOperationCreator paramICreateItemOperationCreator)
  {
    super(paramFrame, paramTCSession, paramNewItemDialog, paramICreateItemOperationCreator);
  }

  public void loadCustomPanels()
  {
    addStepAfter(getItemTypeStep(), new PartitionTypePanelManager());
    addStepAfter(getItemRevMasterFormStep(), new LDFormPanelManager());
    this.templatePanelManager = new EBOPTemplatePanelManager();
    addStepAfter(getItemRevMasterFormStep(), this.templatePanelManager);
  }
}

/* Location:           C:\Siemens\Teamcenter11\portal\plugins\com.teamcenter.rac.cme.ebop.module_11000.2.0.jar
 * Qualified Name:     com.teamcenter.rac.cme.ebop.newproductbop.CreateProductBOPPanel
 * JD-Core Version:    0.6.2
 */