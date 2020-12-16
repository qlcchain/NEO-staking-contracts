using Neo.SmartContract.Framework;
using Neo.SmartContract.Framework.Services.Neo;
using Neo.SmartContract.Framework.Services.System;
using Helper = Neo.SmartContract.Framework.Helper;
using System;
using System.ComponentModel;
using System.Numerics;

namespace QlcSwap
{

    // struct for swap info
    public class SwapInfo
    {
        public byte[] fromAddress;
        public byte[] toAddress;
        public byte[] txid;
        public byte[] userEthAddress;
        public BigInteger amount;
        public BigInteger timestamp;
        public BigInteger blockHeight;
        public uint type;   // 1[USER_TO_CONTRACT], 2[CONTRACT_TO_USER]
    };

    public class Swap : SmartContract
    {

        // Note: This parameter needs to be modified when publishing the main network
		// testnet:ANFnCg69c8VfE36hBhLZRrmofZ9CZU1vqZ
		// mainnet:AJRhyPTBAmGYqbuWTKebPw4C47RNJZ529b
        private static readonly byte[] InitOwner = Helper.ToScriptHash("AJRhyPTBAmGYqbuWTKebPw4C47RNJZ529b");

        // Minimum number of exchanges
        private static readonly BigInteger MinSwapAmount = 0;

        // lock event
        [DisplayName("lockEvent")]
        private static event Action<byte[], BigInteger> LockEvent;

        // unlock event
        [DisplayName("unlockEvent")]
        private static event Action<byte[], BigInteger> UnlockEvent;

        public static object Main(string operation, object[] args)
        {
            if (Runtime.Trigger == TriggerType.Verification)
            {
                return Runtime.CheckWitness(GetOwner());
            }
            else if (Runtime.Trigger == TriggerType.Application)
            {
                byte[] executingScriptHash = ExecutionEngine.ExecutingScriptHash;

                // Add the code associated with the contract invocation here
                switch (operation)
                {
                    case "name": return "Qlc Swap";
                    case "lock":
                        if (args.Length != 3)
                            return "code:0, msg:The lock method need three parameters.";
                        return Lock((byte[])args[0], executingScriptHash, (BigInteger)args[1], (byte[])args[2]);
                    case "init":
                        return Init();
                    case "querySwapInfo":
                        {
                            if (args.Length != 1)
                                return "code:0, msg:The querySwapInfo method need one parameter.";
                            StorageMap SWAP_MAP = Storage.CurrentContext.CreateMap(nameof(SWAP_MAP));
                            var result = SWAP_MAP.Get((byte[])args[0]);
                            if (result.Length == 0)
                            {
                                return "code:0, msg:The swap info not found.";
                            }
                            SwapInfo swapInfo = Helper.Deserialize(result) as SwapInfo;
                            return swapInfo;
                        };
                    case "getOwner":
                        return GetOwner();
                }

                // owner check
                if (!Runtime.CheckWitness(GetOwner()))
                    return "code:0, msg:Only owner.";

                switch (operation)
                {
                    case "unlock":
                        if (args.Length != 4)
                            return "code:0, msg:The unlock method need four parameters.";
                        return Unlock(executingScriptHash, (byte[])args[0], (BigInteger)args[1], (byte[])args[2], (byte[])args[3]);
                    case "transferOwner":
                        if (args.Length != 1)
                            return "code:0, msg:The transferOwner method need one parameter.";
                        return TransferOwner((byte[])args[0]);
                }

            }
            return false;
        }

        // lock
        [DisplayName("lock")]
        private static object Lock(byte[] from, byte[] to, BigInteger amount, byte[] userEthAddress)
        {
            // params length check
            if (from.Length != 20 || userEthAddress.Length != 20)
            {
                return "code:0, msg:The parameters error";
            }

            // amount check
            if (amount <= MinSwapAmount)
            {
                return "code:0, msg:The number of transfers cannot be less than 0";
            }

            // transfer qlc from user to contract
            object[] transferParams = new object[3];
            transferParams[0] = from;
            transferParams[1] = to;
            transferParams[2] = amount;
            if (QlcMain("transfer", transferParams))
            {
                // swap info
                var swapInfo = new SwapInfo()
                {
                    fromAddress = from,
                    toAddress = to,
                    amount = amount,
                    userEthAddress = userEthAddress,
                    timestamp = Blockchain.GetBlock(Blockchain.GetHeight()).Timestamp,
                    blockHeight = Blockchain.GetHeight(),
                    type = 1
                };

                var txid = (ExecutionEngine.ScriptContainer as Transaction).Hash;
                swapInfo.txid = txid;
                StorageMap SWAP_MAP = Storage.CurrentContext.CreateMap(nameof(SWAP_MAP));
                SWAP_MAP.Put(txid, Helper.Serialize(swapInfo));

                // event
                LockEvent(txid, 1);
                return txid;
            }
            else
            {
                return "code:0, msg:transfer error.";
            }

        }

        // unlock
        [DisplayName("unlock")]
        private static object Unlock(byte[] from, byte[] to, BigInteger amount, byte[] userEthAddress, byte[] ethTxid)
        {
            // params length check
            if (to.Length != 20 || ethTxid.Length == 0)
            {
                return "code:0, msg:The parameters error";
            }

            // amount check
            if (amount <= MinSwapAmount)
            {
                return "code:0, msg:The number of transfers cannot be less than 0";
            }
			
            // Whether ethTxid already exists
            StorageMap SWAP_MAP = Storage.CurrentContext.CreateMap(nameof(SWAP_MAP));
            var result = SWAP_MAP.Get(ethTxid);
            if (result.Length != 0)
            {
                return "code:0, msg:The ethTxid already exists.";
            }

            // transfer qlc from user to contract
            object[] transferParams = new object[3];
            transferParams[0] = from;
            transferParams[1] = to;
            transferParams[2] = amount;
            if (QlcMain("transfer", transferParams))
            {
                // swap info
                var swapInfo = new SwapInfo()
                {
                    fromAddress = from,
                    toAddress = to,
                    amount = amount,
                    userEthAddress = userEthAddress,
                    timestamp = Blockchain.GetBlock(Blockchain.GetHeight()).Timestamp,
                    blockHeight = Blockchain.GetHeight(),
                    type = 2
                };

                var txid = (ExecutionEngine.ScriptContainer as Transaction).Hash;
                swapInfo.txid = txid;
                SWAP_MAP.Put(ethTxid, Helper.Serialize(swapInfo));

                // event
                UnlockEvent(ethTxid, 2);
                return txid;
            }
            else
            {
                return "code:0, msg:transfer error.";
            }

        }

        // initialization parameters, only once
        [DisplayName("init")]
        private static bool Init()
        {
            byte[] owner = Storage.Get(Storage.CurrentContext, "Owner");
            if (owner.Length != 0) return false;
            Storage.Put(Storage.CurrentContext, "Owner", InitOwner);
            return true;
        }

        // transfer Owner
        [DisplayName("transferOwner")]
        private static object TransferOwner(byte[] newOwner)
        {
            // new owner address
            if (newOwner.Length != 20)
            {
                return "code:0, msg:The new Owner error.";
            }
			// 验证地址合法性
			// 合约内部暂时只能对长度进行验证，可以分两步走，第一步先set，判断没问题后再change
            Storage.Put(Storage.CurrentContext, "Owner", newOwner);
            return "code:1, msg:transfer success.";

        }

        // return storage owner
        private static byte[] GetOwner()
        {
            return Storage.Get(Storage.CurrentContext, "Owner");
        }

        // mainnet qlc hash： 0d821bd7b6d53f5c2b40e217c6defc8bbe896cf5
        // testnet qlc hash： b9d7ea3062e6aeeb3e8ad9548220c4ba1361d263
        [Appcall("0d821bd7b6d53f5c2b40e217c6defc8bbe896cf5")]
        private static extern bool QlcMain(string operation, params object[] args);
    }
}
