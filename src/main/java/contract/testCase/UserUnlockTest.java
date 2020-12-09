/** 
 * @Package contract.testCase 
 * @Description 
 * @author yfhuang521@gmail.com
 * @date 2020年9月1日 下午3:03:40 
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

/** 
 * @Description user unlock
 * @author yfhuang521@gmail.com
 * @date 2020年9月1日 下午3:03:40 
 */
public class UserUnlockTest {
	
	Neow3j neow3j = Neow3j.build(new HttpService("http://seed2.ngd.network:20332"));
	
    ScriptHash contractScripthash = new ScriptHash("84be590a68903cdc37c6afe62e9056c70cc22f0e");
    
    String ownerWif = "";
    
    /**
     * 
     * @Description  Timeout call
     * @return void  
     * @author yfhuang521@gmail.com
     * @date 2020年9月1日 下午3:04:27
     */
	@Test
    public void userUnlock1() {

        try {

            Account account = Account.fromWIF("your wif").build();
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
                    .function("userUnlock")
                    .parameter(ContractParameter.string("1be1d4114e1942c9a0a5d7a590d144f5"))
                    .parameter(ContractParameter.byteArrayFromAddress("AJ5huRnZJj3DZSxnJuZhAMLW1wfc8oMztj"))
                    .attributes(attributes)
                    .account(account)
                    //.networkFee(0.001)
                    .build();

            InvocationTransaction tx = contractInvocation.getTransaction();

            RawScript witnessTo = RawScript.createWitness(tx.toArrayWithoutScripts(), account.getECKeyPair());
            RawScript witnessFrom = new RawScript(new ScriptBuilder().pushData("1be1d4114e1942c9a0a5d7a590d144f5").pushInteger(1).opCode(OpCode.PACK).pushData("userUnlock").toArray() , contractScripthash);

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
     * @Description  Incorrect text
     * @return void  
     * @author yfhuang521@gmail.com
     * @date 2020年9月1日 下午3:04:27
     */
	@Test
    public void userUnlock2() {

        try {

            Account account = Account.fromWIF("your wif").build();
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
                    .function("userUnlock")
                    .parameter(ContractParameter.string("1ee359864e8a4b399c241e871c08b21t"))
                    .parameter(ContractParameter.byteArrayFromAddress("AJ5huRnZJj3DZSxnJuZhAMLW1wfc8oMztj"))
                    .attributes(attributes)
                    .account(account)
                    //.networkFee(0.001)
                    .build();

            InvocationTransaction tx = contractInvocation.getTransaction();

            RawScript witnessTo = RawScript.createWitness(tx.toArrayWithoutScripts(), account.getECKeyPair());
            RawScript witnessFrom = new RawScript(new ScriptBuilder().pushData("1ee359864e8a4b399c241e871c08b21f").pushInteger(1).opCode(OpCode.PACK).pushData("userUnlock").toArray() , contractScripthash);

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
     * @Description  Lack of parameter
     * @return void  
     * @author yfhuang521@gmail.com
     * @date 2020年9月1日 下午3:04:27
     */
	@Test
    public void userUnlock4() {

        try {

            Account account = Account.fromWIF("your wif").build();
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
                    .function("userUnlock")
                    .parameter(ContractParameter.string("41d5a36ef5e7487f9f8abbb3c48a4a2b"))
                    //.parameter(ContractParameter.byteArrayFromAddress("AJ5huRnZJj3DZSxnJuZhAMLW1wfc8oMztj"))
                    .attributes(attributes)
                    .account(account)
                    //.networkFee(0.001)
                    .build();

            InvocationTransaction tx = contractInvocation.getTransaction();

            RawScript witnessTo = RawScript.createWitness(tx.toArrayWithoutScripts(), account.getECKeyPair());
            RawScript witnessFrom = new RawScript(new ScriptBuilder().pushData("41d5a36ef5e7487f9f8abbb3c48a4a2b").pushInteger(1).opCode(OpCode.PACK).pushData("userUnlock").toArray() , contractScripthash);

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
     * @Description Normal call/repeat call
     * @return void  
     * @author yfhuang521@gmail.com
     * @date 2020年9月1日 下午3:04:27
     */
	@Test
    public void userUnlock56() {

        try {

            Account account = Account.fromWIF("your wif").build();
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
                    .function("userUnlock")
                    .parameter(ContractParameter.string("41d5a36ef5e7487f9f8abbb3c48a4a2b"))
                    .parameter(ContractParameter.byteArrayFromAddress("AJ5huRnZJj3DZSxnJuZhAMLW1wfc8oMztj"))
                    .attributes(attributes)
                    .account(account)
                    //.networkFee(0.001)
                    .build();

            InvocationTransaction tx = contractInvocation.getTransaction();

            RawScript witnessTo = RawScript.createWitness(tx.toArrayWithoutScripts(), account.getECKeyPair());
            RawScript witnessFrom = new RawScript(new ScriptBuilder().pushData("41d5a36ef5e7487f9f8abbb3c48a4a2b").pushInteger(1).opCode(OpCode.PACK).pushData("userUnlock").toArray() , contractScripthash);

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
	
	public static void main(String[] args) {

        Account account = Account.fromWIF("your wif").build();
		System.out.println(account.getAddress());
	}
	
}
