/*     */ package com.project.dialogs;
/*     */ 
/*     */ import com.project.utils.DatasetFinder;
/*     */ import com.project.utils.DateUtils;
/*     */ import com.teamcenter.rac.aif.AIFDesktop;
/*     */ import com.teamcenter.rac.kernel.TCAttachmentScope;
/*     */ import com.teamcenter.rac.kernel.TCCRDecision;
/*     */ import com.teamcenter.rac.kernel.TCComponent;
/*     */ import com.teamcenter.rac.kernel.TCComponentDataset;
/*     */ import com.teamcenter.rac.kernel.TCComponentGroupMember;
/*     */ import com.teamcenter.rac.kernel.TCComponentSignoff;
/*     */ import com.teamcenter.rac.kernel.TCComponentTask;
/*     */ import com.teamcenter.rac.kernel.TCComponentTcFile;
/*     */ import com.teamcenter.rac.kernel.TCComponentUser;
/*     */ import com.teamcenter.rac.kernel.TCException;
/*     */ import com.teamcenter.rac.kernel.TCPreferenceService;
/*     */ import com.teamcenter.rac.kernel.TCSession;
/*     */ import com.teamcenter.rac.workflow.commands.newperformsignoff.DecisionDialog;
/*     */ import java.io.File;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Date;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.swing.ImageIcon;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.JRadioButton;
/*     */ 
/*     */ public class NewDecisionDialog extends DecisionDialog
/*     */ {
/*     */   static ImageIcon approveIcon;
/*     */ 
/*     */   public NewDecisionDialog(AIFDesktop arg0, TCComponentTask arg1, TCComponentSignoff arg2)
/*     */   {
/*  45 */     super(arg0, arg1, arg2);
/*     */   }
/*     */ 
/*     */   public void initializeDialog() {
/*  49 */     super.initializeDialog();
/*  50 */     this.lblIcon.setIcon(approveIcon);
/*  51 */     this.rbApprove.setSelected(true);
/*     */   }
/*     */ 
/*     */   public void commitDecision()
/*     */   {
/*  60 */     if (this.decision == TCCRDecision.APPROVE_DECISION) {
/*     */       try {
/*  62 */         String processName = this.psTask.getParent().getRoot().getName();
/*  63 */         String taskName = this.psTask.getParent().getName();
/*  64 */         TCPreferenceService prefSvc = this.session.getPreferenceService();
/*     */ 
/*  68 */         String[] signatureConditions = prefSvc.getStringArray(
/*  69 */           0, 
/*  70 */           "CUST_signature_process_tasks");
/*  71 */         if ((signatureConditions != null) && 
/*  72 */           (signatureConditions.length > 0)) {
/*  73 */           for (int i = 0; i < signatureConditions.length; i++) {
/*  74 */             String[] conditions = signatureConditions[i].split(",");
/*  75 */             if ((conditions[0].equals(processName)) && 
/*  76 */               (conditions[1].equals(taskName))) {
/*  77 */               System.out.println(conditions[0] + "," + 
/*  78 */                 conditions[1] + conditions[2] + "," + 
/*  79 */                 processName + " size=" + 
/*  80 */                 conditions.length);
/*  81 */               String[] datasetTypes = conditions[2].split("-");
/*     */ 
/*  83 */               TCComponent[] targetComponents = this.psTask
/*  84 */                 .getAttachments(TCAttachmentScope.GLOBAL, 
/*  85 */                 1);
/*  86 */               File dataFile = new File(
/*  87 */                 System.getProperty("java.io.tmpdir"), 
/*  88 */                 "SignoffInfo.txt");
/*  89 */               dataFile.createNewFile();
/*  90 */               PrintWriter pw = new PrintWriter(
/*  91 */                 new FileOutputStream(dataFile));
/*  92 */               Map map = getFlowSignInfo(this.psTask, taskName);
/*  93 */               if (map != null) {
/*  94 */                 Iterator itr = map.keySet().iterator();
/*  95 */                 while (itr.hasNext()) {
/*  96 */                   Object object = itr.next();
/*  97 */                   pw.print(object + "=" + map.get(object) + 
/*  98 */                     "|");
/*     */                 }
/*     */               }
/* 101 */               pw.println();
/* 102 */               pw.close();
/* 103 */               for (int j = 0; j < targetComponents.length; j++) {
/* 104 */                 TCComponent target = targetComponents[j];
/* 105 */                 String targetType = target.getType();
/* 106 */                 for (int k = 0; k < datasetTypes.length; k++) {
/* 107 */                   if (targetType.equals(datasetTypes[k])) {
/* 108 */                     TCComponentDataset dataset = (TCComponentDataset)target;
/* 109 */                     File signFile = getUserSignDoc();
/* 110 */                     String signPath = "";
/* 111 */                     if (signFile != null) {
/* 112 */                       signPath = signFile
/* 113 */                         .getAbsolutePath();
/*     */                     }
/*     */ 
/* 116 */                     if (targetType.startsWith("MSWord")) {
/* 117 */                       File docFile = getSignExeFile("word");
/* 118 */                       if (docFile != null) {
/* 119 */                         execSignDocument(dataset, 
/* 120 */                           docFile, dataFile, 
/* 121 */                           "word", signPath);
/*     */                       }
/*     */ 
/*     */                     }
/* 125 */                     else if (targetType
/* 125 */                       .startsWith("MSExcel")) {
/* 126 */                       File excelFile = getSignExeFile("excel");
/* 127 */                       if (excelFile != null) {
/* 128 */                         execSignDocument(dataset, 
/* 129 */                           excelFile, dataFile, 
/* 130 */                           "excel", signPath);
/*     */                       }
/*     */ 
/*     */                     }
/* 134 */                     else if (targetType
/* 134 */                       .equals("U2_AutoCAD")) {
/* 135 */                       File autoFile = getSignExeFile("autocad");
/* 136 */                       if (autoFile != null) {
/* 137 */                         execSignDocument(dataset, 
/* 138 */                           autoFile, dataFile, 
/* 139 */                           "U2_AutoCAD", signPath);
/*     */                       }
/*     */ 
/*     */                     }
/* 143 */                     else if (targetType
/* 143 */                       .equals("CATDrawing")) {
/* 144 */                       System.out.println("进入CATIA签名");
/* 145 */                       File autoFile = getSignExeFile("catdrawing");
/* 146 */                       if (autoFile != null) {
/* 147 */                         execSignDocument(dataset, 
/* 148 */                           autoFile, dataFile, 
/* 149 */                           "catdrawing", signPath);
/*     */                       }
/*     */                     }
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (Exception ex)
/*     */       {
/* 161 */         ex.printStackTrace();
/*     */       }
/*     */     }
/* 164 */     super.commitDecision();
/*     */   }
/*     */ 
/*     */   private boolean execSignDocument(TCComponentDataset dataset, File scriptFile, File dataFile, String type, String signPath)
/*     */     throws Exception
/*     */   {
/* 179 */     File[] files = dataset.getFiles(type);
/* 180 */     if ((files == null) || (files.length == 0))
/* 181 */       return false;
/* 182 */     for (int n = 0; n < files.length; n++)
/*     */     {
/* 184 */       String cmdStr = null;
/* 185 */       if ((signPath.equals("")) || (signPath == null))
/* 186 */         cmdStr = "Wscript.exe \"" + scriptFile.getAbsolutePath() + 
/* 187 */           "\" \"" + files[n].getAbsolutePath() + "\" \"" + 
/* 188 */           dataFile.getAbsolutePath() + "\"";
/*     */       else
/* 190 */         cmdStr = "Wscript.exe \"" + scriptFile.getAbsolutePath() + 
/* 191 */           "\" \"" + files[n].getAbsolutePath() + "\" \"" + 
/* 192 */           dataFile.getAbsolutePath() + "\" \"" + signPath + 
/* 193 */           "\"";
/* 194 */       if ((signPath == null) || (signPath.equals("")))
/* 195 */         signPath = "false";
/* 196 */       if (type.equals("U2_AutoCAD"))
/*     */       {
/* 198 */         cmdStr = scriptFile.getAbsolutePath() + " " + 
/* 199 */           files[n].getAbsolutePath() + "," + 
/* 200 */           dataFile.getAbsolutePath() + "," + signPath + 
/* 201 */           ",false,false,false";
/* 202 */       } else if (type.equals("catdrawing")) {
/* 203 */         cmdStr = scriptFile.getAbsolutePath() + " " + 
/* 204 */           files[n].getAbsolutePath() + "," + 
/* 205 */           dataFile.getAbsolutePath() + "," + signPath + 
/* 206 */           ",false,false,false";
/*     */       }
/* 208 */       System.out.println("执行路径：" + cmdStr);
/* 209 */       Process process = Runtime.getRuntime().exec(cmdStr);
/* 210 */       process.waitFor();
/* 211 */       TCComponentTcFile tcfile = (TCComponentTcFile)dataset
/* 212 */         .getRelatedComponent("ref_list");
/* 213 */       tcfile.getProperty("original_file_name");
/* 214 */       File datefile = new File(files[n].getAbsolutePath());
/* 215 */       new File[] { datefile };
/* 216 */       String ss = dataset.getType();
/* 217 */       if (("MSWordX".equals(ss)) || ("MSWord".equals(ss))) {
/* 218 */         dataset.removeFiles("word");
/* 219 */         dataset.setFiles(new String[] { datefile.getAbsolutePath() }, 
/* 220 */           new String[] { type });
/* 221 */       } else if (("MSExcelX".equals(ss)) || ("MSExcel".equals(ss))) {
/* 222 */         dataset.removeFiles("excel");
/* 223 */         dataset.setFiles(new String[] { datefile.getAbsolutePath() }, 
/* 224 */           new String[] { type });
/* 225 */       } else if ("U2_AutoCAD".equals(ss)) {
/* 226 */         dataset.removeFiles("U2_AutoCAD");
/* 227 */         dataset.setFiles(new String[] { datefile.getAbsolutePath() }, 
/* 228 */           new String[] { "U2_AutoCAD" });
/* 229 */       } else if ("CATDrawing".equals(ss)) {
/* 230 */         dataset.removeFiles("catdrawing");
/* 231 */         dataset.setFiles(new String[] { datefile.getAbsolutePath() }, 
/* 232 */           new String[] { "catdrawing" });
/*     */       }
/*     */     }
/* 235 */     return true;
/*     */   }
/*     */ 
/*     */   private File getSignExeFile(String exeType)
/*     */   {
/* 244 */     File scriptFile = null;
/* 245 */     DatasetFinder finder = new DatasetFinder(this.session);
/* 246 */     TCComponentDataset scriptDataset = finder.FindDatasetByName(
/* 247 */       "SignScripts", "Text");
/* 248 */     if (scriptDataset == null) {
/* 249 */       return null;
/*     */     }
/* 251 */     if (exeType.equals("word")) {
/* 252 */       scriptFile = finder.exportFileToDir(scriptDataset, "Text", 
/* 253 */         "SubsMacros-MSWord.wsf", 
/* 254 */         System.getProperty("java.io.tmpdir"));
/* 255 */     } else if (exeType.equals("excel")) {
/* 256 */       scriptFile = finder.exportFileToDir(scriptDataset, "Text", 
/* 257 */         "SubsMacros-MSExcel.wsf", 
/* 258 */         System.getProperty("java.io.tmpdir"));
/*     */     }
/* 260 */     else if (exeType.equals("autocad")) {
/* 261 */       scriptFile = finder.exportFileToDir(scriptDataset, "Text", 
/* 262 */         "VBAutoCAD.exe", System.getProperty("java.io.tmpdir"));
/*     */     }
/* 264 */     else if (exeType.equals("catdrawing")) {
/* 265 */       scriptFile = finder.exportFileToDir(scriptDataset, "Text", 
/* 266 */         "VBCatia.exe", System.getProperty("java.io.tmpdir"));
/*     */ 
/* 268 */       finder.exportFileToDir(scriptDataset, "Text", 
/* 269 */         "Interop.DRAFTINGITF.dll", System.getProperty("java.io.tmpdir"));
/* 270 */       finder.exportFileToDir(scriptDataset, "Text", 
/* 271 */         "Interop.INFITF.dll", System.getProperty("java.io.tmpdir"));
/* 272 */       finder.exportFileToDir(scriptDataset, "Text", 
/* 273 */         "Interop.MECMOD.dll", System.getProperty("java.io.tmpdir"));
/*     */     }
/* 275 */     return scriptFile;
/*     */   }
/*     */ 
/*     */   private Map getFlowSignInfo(TCComponentTask root, String taskName)
/*     */     throws TCException
/*     */   {
/* 288 */     Map map = new HashMap();
/* 289 */     root.getName();
/* 290 */     TCComponentTask[] subTasks = root.getSubtasks();
/* 291 */     List reviewTasks = new ArrayList();
/* 292 */     List doTasks = new ArrayList();
/* 293 */     List routeTasks = new ArrayList();
/* 294 */     String allUserName = "";
/* 295 */     String allDateStr = "";
/* 296 */     if (subTasks != null) {
/* 297 */       for (int i = 0; i < subTasks.length; i++) {
/* 298 */         if (("EPMReviewTask".equals(subTasks[i].getTaskType())) || 
/* 299 */           ("EPMAcknowledgeTask".equals(
/* 300 */           subTasks[i].getTaskType())))
/* 301 */           reviewTasks.add(subTasks[i]);
/* 302 */         if ("EPMDoTask".equals(subTasks[i].getTaskType())) {
/* 303 */           System.out.println("流程名称：" + subTasks[i].getName());
/* 304 */           doTasks.add(subTasks[i]);
/*     */         }
/* 306 */         if ("EPMRouteTask".equals(subTasks[i].getTaskType()))
/* 307 */           routeTasks.add(subTasks[i]);
/*     */       }
/*     */     }
/* 310 */     if (subTasks.length > 0)
/*     */     {
/* 312 */       for (int i = 0; i < doTasks.size(); i++) {
/* 313 */         TCComponentTask doTask = (TCComponentTask)doTasks.get(i);
/* 314 */         doTask.refresh();
/* 315 */         taskName = doTask.getName();
/* 316 */         boolean done = doTask.getLogicalProperty("done");
/* 317 */         Date last_mod_date = doTask.getDateProperty("last_mod_date");
/* 318 */         if (done) {
/* 319 */           allDateStr = DateUtils.getDateYMD(last_mod_date);
/* 320 */           TCComponentUser respParty = (TCComponentUser)doTask
/* 321 */             .getResponsibleParty();
/* 322 */           allUserName = respParty.getProperty("user_name");
/* 323 */           map.put(taskName + ".Name", allUserName);
/* 324 */           map.put(taskName + ".Date", allUserName);
/*     */         }
/*     */       }
/* 327 */       for (int i = 0; i < routeTasks.size(); i++) {
/* 328 */         TCComponentTask routeTask = (TCComponentTask)routeTasks.get(i);
/* 329 */         taskName = routeTask.getName();
/* 330 */         TCComponentTask[] routeSubTasks = routeTask.getSubtasks();
/* 331 */         int routeNo = 0;
/* 332 */         for (int j = 0; j < routeSubTasks.length; j++) {
/* 333 */           TCComponentTask performSignoffsSubTask = routeSubTasks[j]
/* 334 */             .getSubtask("perform-signoffs");
/* 335 */           if (performSignoffsSubTask != null) {
/* 336 */             performSignoffsSubTask.refresh();
/* 337 */             TCComponentSignoff[] validSignoffs = performSignoffsSubTask
/* 338 */               .getValidSignoffs();
/* 339 */             if ((validSignoffs != null) && (validSignoffs.length >= 1))
/*     */             {
/* 342 */               for (int k = 0; k < validSignoffs.length; k++) {
/* 343 */                 if ((validSignoffs[k].getDecision() != null) && 
/* 344 */                   (validSignoffs[k].getDecision() == TCCRDecision.APPROVE_DECISION)) {
/* 345 */                   routeNo++;
/* 346 */                   String userName = validSignoffs[k]
/* 347 */                     .getGroupMember().getUser()
/* 348 */                     .getProperty("user_name");
/*     */ 
/* 350 */                   map.put(taskName + routeNo + ".Name", userName);
/* 351 */                   Date decisionDate = validSignoffs[k]
/* 352 */                     .getDecisionDate();
/* 353 */                   if (decisionDate != null) {
/* 354 */                     String dateStr = 
/* 355 */                       DateUtils.getDateYMD(decisionDate);
/* 356 */                     map.put(taskName + routeNo + ".Date", 
/* 357 */                       dateStr);
/*     */                   }
/*     */                 }
/*     */               }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */ 
/* 366 */       for (int i = 0; i < reviewTasks.size(); i++) {
/* 367 */         TCComponentTask reviewTask = 
/* 368 */           (TCComponentTask)reviewTasks
/* 368 */           .get(i);
/* 369 */         TCComponentTask performSignoffsSubTask = reviewTask
/* 370 */           .getSubtask("perform-signoffs");
/* 371 */         taskName = reviewTask.getName();
/* 372 */         performSignoffsSubTask.refresh();
/* 373 */         TCComponentSignoff[] validSignoffs = performSignoffsSubTask
/* 374 */           .getValidSignoffs();
/* 375 */         int j = 0;
/*     */ 
/* 377 */         while (j < validSignoffs.length)
/*     */         {
/* 379 */           if ((validSignoffs[j].getDecision() != null) && 
/* 380 */             (validSignoffs[j].getDecision() == TCCRDecision.APPROVE_DECISION)) {
/* 381 */             String userName = validSignoffs[j].getGroupMember()
/* 382 */               .getUser().getProperty("user_name");
/* 383 */             if (allUserName.equals(""))
/* 384 */               allUserName = userName;
/*     */             else {
/* 386 */               allUserName = allUserName + "," + userName;
/*     */             }
/* 388 */             String dateStr = "";
/* 389 */             Date decisionDate = validSignoffs[j].getDecisionDate();
/* 390 */             if (decisionDate != null)
/* 391 */               dateStr = DateUtils.getDateYMD(decisionDate);
/*     */             else {
/* 393 */               dateStr = "NoDicisionDate";
/*     */             }
/* 395 */             if (allDateStr.equals(""))
/* 396 */               allDateStr = dateStr;
/*     */             else {
/* 398 */               allDateStr = allDateStr + "," + dateStr;
/*     */             }
/*     */           }
/* 401 */           j++;
/*     */         }
/* 403 */         if ((allUserName == null) || (allUserName.equals(""))) {
/* 404 */           String userName = performSignoffsSubTask.getSession()
/* 405 */             .getUser().getProperty("user_name");
/* 406 */           allUserName = userName;
/* 407 */           allDateStr = DateUtils.getDateYMD(new Date());
/*     */         }
/* 409 */         map.put(taskName + ".Name", allUserName);
/* 410 */         map.put(taskName + ".Date", allDateStr);
/*     */       }
/*     */     } else {
/* 413 */       allUserName = this.session.getUser().getProperty("user_name");
/* 414 */       map.put(taskName + ".Name", allUserName);
/* 415 */       map.put(taskName + ".Date", DateUtils.getDateYMD(new Date()));
/*     */     }
/* 417 */     return map;
/*     */   }
/*     */ 
/*     */   private File getUserSignDoc()
/*     */   {
/*     */     try
/*     */     {
/* 428 */       String userId = this.session.getUser().getUserId();
/* 429 */       File docFile = null;
/* 430 */       DatasetFinder finder = new DatasetFinder(this.session);
/* 431 */       TCComponentDataset dataset = finder.FindDatasetByName(userId, 
/* 432 */         "Image");
/* 433 */       if (dataset == null) {
/* 434 */         return null;
/*     */       }
/* 436 */       return finder.exportFileToDir(dataset, "Image", userId + 
/* 437 */         ".jpg", System.getProperty("java.io.tmpdir"));
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/* 441 */     return null;
/*     */   }
/*     */ }

/* Location:           D:\jd-gui-0.3.3.windows\com.Sign_1.0.0.201512091610.jar
 * Qualified Name:     com.project.dialogs.NewDecisionDialog
 * JD-Core Version:    0.6.2
 */