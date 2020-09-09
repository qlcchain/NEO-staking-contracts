/** 
 * @Package contract.testCase 
 * @Description 
 * @author yfhuang521@gmail.com
 * @date 2020年9月1日 下午4:12:55 
 * @version V1.0 
 */ 
package contract.testCase;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import io.neow3j.constants.OpCode;
import io.neow3j.contract.ContractInvocation;
import io.neow3j.contract.ContractParameter;
import io.neow3j.contract.ScriptBuilder;
import io.neow3j.contract.ScriptHash;
import io.neow3j.crypto.transaction.RawScript;
import io.neow3j.crypto.transaction.RawTransactionAttribute;
import io.neow3j.model.types.TransactionAttributeUsageType;
import io.neow3j.protocol.Neow3j;
import io.neow3j.protocol.core.methods.response.StackItem;
import io.neow3j.protocol.exceptions.ErrorResponseException;
import io.neow3j.protocol.http.HttpService;
import io.neow3j.transaction.InvocationTransaction;
import io.neow3j.wallet.Account;
import qlc.utils.Helper;

/** 
 * @Description refund nep5 token to wrapper
 * @author yfhuang521@gmail.com
 * @date 2020年9月1日 下午4:12:55 
 */
public class RefundWrapperTest {
	
	Neow3j neow3j = Neow3j.build(new HttpService("http://seed2.ngd.network:20332"));
	
    ScriptHash contractScripthash = new ScriptHash("84be590a68903cdc37c6afe62e9056c70cc22f0e");
    
    String ownerWif = "";
    
    /**
     * 
     * @Description No timeout call/normal call/repeat call
     * @return void  
     * @author yfhuang521@gmail.com
     * @date 2020年9月1日 下午4:14:50
     */
    @Test
    public void refundWrapper123() {
        try {
            Account account = Account.fromWIF(ownerWif).build();
            account.updateAssetBalances(neow3j);

            //attributes - to & contract address
            List<RawTransactionAttribute> attributes = Arrays.asList(
                    new RawTransactionAttribute(
                            TransactionAttributeUsageType.SCRIPT,
                            account.getScriptHash().toArray()),
                    new RawTransactionAttribute(TransactionAttributeUsageType.SCRIPT,
                            contractScripthash.toArray())
            );
            ContractInvocation contractInvocation = new ContractInvocation.Builder(neow3j).contractScriptHash(contractScripthash)
                    .function("refundWrapper")
                    .parameter(ContractParameter.byteArray("3d478d0c9aaca823e2b4a1e3f0ff56611e21be69020f2d69779dcb243d0b0bc2"))
                    .attributes(attributes)
                    .account(account)
                    //.networkFee(0.001)
                    .build();
            InvocationTransaction tx = contractInvocation.getTransaction();
            RawScript witnessTo = RawScript.createWitness(tx.toArrayWithoutScripts(), account.getECKeyPair());
            // call contract verification Main("refundUser, ["ddbda109309f9fafa6dd6a9cb9f1df40"]")
            RawScript witnessFrom = new RawScript(new ScriptBuilder()
            						.pushData(Helper.hexStringToBytes("3d478d0c9aaca823e2b4a1e3f0ff56611e21be69020f2d69779dcb243d0b0bc2"))
            						//.pushData("b6461812369dfa994f7926beeaa4b3e36579d591616b2ae1494b94865de2da0f")
            						.pushInteger(1).opCode(OpCode.PACK)
            						.pushData("refundWrapper").toArray(), contractScripthash);

            tx.addScript(witnessTo);
            tx.addScript(witnessFrom);

            contractInvocation.invoke();
            System.out.println(contractInvocation.getResponse().getResult());
            System.out.println(contractInvocation.getTransaction().getTxId());
            List<StackItem> sItems = contractInvocation.testInvoke().getStack();
            if (sItems.size() > 0) {
                StackItem item = sItems.get(0);
                System.out.println(item.asByteArray().getAsString());
            }
        } catch (ErrorResponseException e) {
            System.out.println(e.getError().getMessage());
            e.printStackTrace();
        } catch (IOException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
	}
    
    /**
     * 
     * @Description Incorrect text
     * @return void  
     * @author yfhuang521@gmail.com
     * @date 2020年9月1日 下午4:14:50
     */
    @Test
    public void refundWrapper4() {
        try {
            Account account = Account.fromWIF(ownerWif).build();
            account.updateAssetBalances(neow3j);

            //attributes - to & contract address
            List<RawTransactionAttribute> attributes = Arrays.asList(
                    new RawTransactionAttribute(
                            TransactionAttributeUsageType.SCRIPT,
                            account.getScriptHash().toArray()),
                    new RawTransactionAttribute(TransactionAttributeUsageType.SCRIPT,
                            contractScripthash.toArray())
            );
            ContractInvocation contractInvocation = new ContractInvocation.Builder(neow3j).contractScriptHash(contractScripthash)
                    .function("refundWrapper")
                    .parameter(ContractParameter.byteArray("3d478d0c9aaca823e2b4a1e3f0ff56611e21be69020f2d69779dcb243d0b0bc4"))
                    .attributes(attributes)
                    .account(account)
                    //.networkFee(0.001)
                    .build();
            InvocationTransaction tx = contractInvocation.getTransaction();
            RawScript witnessTo = RawScript.createWitness(tx.toArrayWithoutScripts(), account.getECKeyPair());
            // call contract verification Main("refundUser, ["ddbda109309f9fafa6dd6a9cb9f1df40"]")
            RawScript witnessFrom = new RawScript(new ScriptBuilder()
            						.pushData(Helper.hexStringToBytes("3d478d0c9aaca823e2b4a1e3f0ff56611e21be69020f2d69779dcb243d0b0bc2"))
            						//.pushData("b6461812369dfa994f7926beeaa4b3e36579d591616b2ae1494b94865de2da0f")
            						.pushInteger(1).opCode(OpCode.PACK)
            						.pushData("refundWrapper").toArray(), contractScripthash);

            tx.addScript(witnessTo);
            tx.addScript(witnessFrom);

            contractInvocation.invoke();
            System.out.println(contractInvocation.getResponse().getResult());
            System.out.println(contractInvocation.getTransaction().getTxId());
            List<StackItem> sItems = contractInvocation.testInvoke().getStack();
            if (sItems.size() > 0) {
                StackItem item = sItems.get(0);
                System.out.println(item.asByteArray().getAsString());
            }
        } catch (ErrorResponseException e) {
            System.out.println(e.getError().getMessage());
            e.printStackTrace();
        } catch (IOException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
	}

    /**
     * 
     * @Description Different token receiving addresses
     * @return void  
     * @author yfhuang521@gmail.com
     * @date 2020年9月1日 下午4:14:50
     */
    @Test
    public void refundWrapper5() {
        try {
            Account account = Account.fromWIF(ownerWif).build();
            account.updateAssetBalances(neow3j);

            //attributes - to & contract address
            List<RawTransactionAttribute> attributes = Arrays.asList(
                    new RawTransactionAttribute(
                            TransactionAttributeUsageType.SCRIPT,
                            account.getScriptHash().toArray()),
                    new RawTransactionAttribute(TransactionAttributeUsageType.SCRIPT,
                            contractScripthash.toArray())
            );
            ContractInvocation contractInvocation = new ContractInvocation.Builder(neow3j).contractScriptHash(contractScripthash)
                    .function("refundWrapper")
                    .parameter(ContractParameter.byteArray("3d478d0c9aaca823e2b4a1e3f0ff56611e21be69020f2d69779dcb243d0b0bc2"))
                    .attributes(attributes)
                    .account(account)
                    //.networkFee(0.001)
                    .build();
            InvocationTransaction tx = contractInvocation.getTransaction();
            RawScript witnessTo = RawScript.createWitness(tx.toArrayWithoutScripts(), account.getECKeyPair());
            // call contract verification Main("refundUser, ["ddbda109309f9fafa6dd6a9cb9f1df40"]")
            RawScript witnessFrom = new RawScript(new ScriptBuilder()
            						.pushData(Helper.hexStringToBytes("3d478d0c9aaca823e2b4a1e3f0ff56611e21be69020f2d69779dcb243d0b0bc2"))
            						//.pushData("b6461812369dfa994f7926beeaa4b3e36579d591616b2ae1494b94865de2da0f")
            						.pushInteger(1).opCode(OpCode.PACK)
            						.pushData("refundWrapper").toArray(), contractScripthash);

            tx.addScript(witnessTo);
            tx.addScript(witnessFrom);

            contractInvocation.invoke();
            System.out.println(contractInvocation.getResponse().getResult());
            System.out.println(contractInvocation.getTransaction().getTxId());
            List<StackItem> sItems = contractInvocation.testInvoke().getStack();
            if (sItems.size() > 0) {
                StackItem item = sItems.get(0);
                System.out.println(item.asByteArray().getAsString());
            }
        } catch (ErrorResponseException e) {
            System.out.println(e.getError().getMessage());
            e.printStackTrace();
        } catch (IOException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
	}

    /**
     * 
     * @Description Lack of parameter
     * @return void  
     * @author yfhuang521@gmail.com
     * @date 2020年9月1日 下午4:14:50
     */
    @Test
    public void refundWrapper6() {
        try {
            Account account = Account.fromWIF(ownerWif).build();
            account.updateAssetBalances(neow3j);

            //attributes - to & contract address
            List<RawTransactionAttribute> attributes = Arrays.asList(
                    new RawTransactionAttribute(
                            TransactionAttributeUsageType.SCRIPT,
                            account.getScriptHash().toArray()),
                    new RawTransactionAttribute(TransactionAttributeUsageType.SCRIPT,
                            contractScripthash.toArray())
            );
            ContractInvocation contractInvocation = new ContractInvocation.Builder(neow3j).contractScriptHash(contractScripthash)
                    .function("refundWrapper")
                    //.parameter(ContractParameter.byteArray("3d478d0c9aaca823e2b4a1e3f0ff56611e21be69020f2d69779dcb243d0b0bc2"))
                    .attributes(attributes)
                    .account(account)
                    //.networkFee(0.001)
                    .build();
            InvocationTransaction tx = contractInvocation.getTransaction();
            RawScript witnessTo = RawScript.createWitness(tx.toArrayWithoutScripts(), account.getECKeyPair());
            // call contract verification Main("refundUser, ["ddbda109309f9fafa6dd6a9cb9f1df40"]")
            RawScript witnessFrom = new RawScript(new ScriptBuilder()
            						.pushData("1")
            						.pushInteger(1).opCode(OpCode.PACK)
            						.pushData("2").toArray(), contractScripthash);

            tx.addScript(witnessTo);
            tx.addScript(witnessFrom);

            contractInvocation.invoke();
            System.out.println(contractInvocation.getResponse().getResult());
            System.out.println(contractInvocation.getTransaction().getTxId());
            List<StackItem> sItems = contractInvocation.testInvoke().getStack();
            if (sItems.size() > 0) {
                StackItem item = sItems.get(0);
                System.out.println(item.asByteArray().getAsString());
            }
        } catch (ErrorResponseException e) {
            System.out.println(e.getError().getMessage());
            e.printStackTrace();
        } catch (IOException e){
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
	}

}
