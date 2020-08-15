/**
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program; if not, write to the Free Software Foundation, Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Copyright (C) 2020 ALGE CONSULTORES <https://www.algeconsultores.com> and contributors (see README.md file).
 */

package ve.net.alge.cxcdobles.component;

import org.adempiere.base.event.IEventTopics;
import org.compiere.model.I_C_BPartner;
import org.compiere.model.I_C_Invoice;
import org.compiere.model.I_C_Payment;
import org.compiere.model.PO;
import org.compiere.model.X_C_BPartner;
import org.compiere.model.X_C_Payment;
import org.compiere.util.CLogger;
import org.compiere.util.DB;
import org.osgi.service.event.Event;

import ve.net.alge.cxcdobles.base.CustomEventFactory;



/**
 * Event Factory
 */
public class EventFactory extends CustomEventFactory {
	
	/**
	 * For initialize class. Register the custom events to build
	 * 
	 * <pre>
	 protected void initialize() {
	 registerEvent(IEventTopics.DOC_BEFORE_COMPLETE, MTableExample.Table_Name, EPrintPluginInfo.class);
	 * }
	 * </pre>
	 */
	@Override
	protected void initialize() {
		
		registerTableEvent(IEventTopics.DOC_AFTER_COMPLETE, I_C_Payment.Table_Name);
		registerTableEvent(IEventTopics.DOC_BEFORE_COMPLETE, I_C_Payment.Table_Name);
		registerTableEvent(IEventTopics.DOC_BEFORE_VOID, I_C_Payment.Table_Name);
		registerTableEvent(IEventTopics.DOC_BEFORE_REACTIVATE, I_C_Payment.Table_Name);
		registerTableEvent(IEventTopics.DOC_BEFORE_REVERSEACCRUAL, I_C_Payment.Table_Name);
		registerTableEvent(IEventTopics.DOC_BEFORE_REVERSECORRECT, I_C_Payment.Table_Name);
		
		}
	
	protected void doHandleEvent(Event event) {
		
		PO po = getPO(event);
		String type = event.getTopic();
		// log.info(po.get_TableName() + " Type: " + type);
		
		if (po.get_TableName().equals(I_C_Payment.Table_Name) && 
				(type.equals(IEventTopics.DOC_BEFORE_COMPLETE) || type.equals(IEventTopics.DOC_BEFORE_PREPARE))) 
			{
			X_C_Payment payment = (X_C_Payment) po;
			if(payment.isReceipt()){
				int value = 0;
				String cadena = payment.getDocumentNo().strip();
				cadena = cadena.replaceAll("\\W+","");
				
				value = DB.getSQLValue(payment.get_TrxName(), "SELECT 1 FROM C_Payment WHERE DocumentNo = ? "
						+ " AND Isreceipt = ? AND C_BankAccount_ID = ? ", cadena,payment.get_ValueAsBoolean("Isreceipt"), payment.get_ValueAsInt("c_bankaccount_id"));
				
				if (value > 0)
					throw new RuntimeException("No. del Documento Ya Existe");
				payment.setDocumentNo(cadena);
				}
			}
		}
	
	}


