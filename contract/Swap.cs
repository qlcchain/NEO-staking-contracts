using Neo.SmartContract.Framework;
using Neo.SmartContract.Framework.Services.Neo;
using Neo.SmartContract.Framework.Services.System;
using Helper = Neo.SmartContract.Framework.Helper;
using System;
using System.ComponentModel;
using System.Numerics;

namespace QlcSwap {


    // struct for swap info
    public class SwapInfo {
        public string originText;
        public byte[] userNeoAddress;
        public byte[] wrapperNeoAddress;
        public byte[] userEthAddress;
        public byte[] txidIn;
        public byte[] txidOut;
        public byte[] txidRefund;
        public BigInteger amount;
        public BigInteger lockTimestamp;
        public BigInteger unLockTimestamp;
        public BigInteger refundTimestamp;
        public BigInteger blockHeight;
        public BigInteger overtimeBlocks;
        public uint type;
        public uint state;
    };

    public class Swap : SmartContract {

        // Note: This parameter needs to be modified when publishing the main network
        private static readonly byte[] InitOwner = Helper.ToScriptHash("ANFnCg69c8VfE36hBhLZRrmofZ9CZU1vqZ");
    
		// Minimum number of exchanges
        // Note: This parameter needs to be modified when publishing the main network
        private static readonly BigInteger MinSwapAmount = 1 * 100000000;
        
        // NEP5 to ERC20 overtime min blocks
        // Note: This parameter needs to be modified when publishing the main network
        private static readonly BigInteger NEP5ToERC20OvertimeBlocks = 40;

        // ERC20 to NEP5 overtime min blocks
        // Note: This parameter needs to be modified when publishing the main network
        private static readonly BigInteger ERC20ToNEP5OvertimeBlocks = 20;

        // user lock event
        [DisplayName("userLockEvent")]
        private static event Action<byte[], BigInteger> UserLockEvent;

        // wrapper unlock event
        [DisplayName("wrapperUnlockEvent")]
        private static event Action<byte[], BigInteger> WrapperUnlockEvent;
        
        // refund user event
        [DisplayName("refundUserEvent")]
        private static event Action<byte[], BigInteger> RefundUserEvent;
        
        // wrapper lock event
        [DisplayName("wrapperLockEvent")]
        private static event Action<byte[], BigInteger> WrapperLockEvent;
        
        // user unlock event
        [DisplayName("userUnlockEvent")]
        private static event Action<byte[], BigInteger> UserUnlockEvent;
        
        // refund wrapper event
        [DisplayName("refundWrapperEvent")]
        private static event Action<byte[], BigInteger> RefundWrapperEvent;

        public static object Main(string operation, object[] args) {
            if (Runtime.Trigger == TriggerType.Verification) {
                return Runtime.CheckWitness(GetOwner());
            } else if (Runtime.Trigger == TriggerType.Application) {
                byte[] executingScriptHash = ExecutionEngine.ExecutingScriptHash;
                
                //Add the code associated with the contract invocation here
                switch (operation) {
                    case "name": return "Qlc Swap";
                    case "userLock":
                        if (args.Length != 5)
                            return "code:0, msg:The userLock method need five parameters.";

                        return UserLock((byte[])args[0], (byte[])args[1], executingScriptHash, (BigInteger)args[2], (byte[])args[3], (BigInteger)args[4]);
                    case "wrapperLock":
                        if (args.Length != 5)
                            return "code:0, msg:The wrapperLock method need four parameters.";
                        return WrapperLock((byte[])args[0], (byte[])args[1], executingScriptHash, (BigInteger)args[2], (byte[])args[3], (BigInteger)args[4]);
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
                        }

                    case "getOwner":
                        return GetOwner();
                }

                // owner check
                if (!Runtime.CheckWitness(GetOwner()))
                    return "code:0, msg:Only owner.";

                switch (operation) {
                    case "wrapperUnlock":
                        if (args.Length != 2)
                            return "code:0, msg:The wrapperUnlock method need two parameters.";
                        return WrapperUnlock((string)args[0], executingScriptHash, (byte[])args[1]);
                    case "refundUser":
                        if (args.Length != 1)
                            return "code:0, msg:The refundUser method need one parameter.";
                        return RefundUser((string)args[0], executingScriptHash);
                    case "userUnlock":
                        if (args.Length != 2)
                            return "code:0, msg:The userUnlock method need one parameter.";
                        return UserUnlock((string)args[0], executingScriptHash, (byte[])args[1]);
                    case "refundWrapper":
                        if (args.Length != 1)
                            return "code:0, msg:The refundWrapper method need one parameter.";
                        return RefundWrapper((byte[])args[0], executingScriptHash);
                    case "deleteSwapInfo":
                        if (args.Length != 1)
                            return "code:0, msg:The deleteSwapInfo method need one parameter.";
                        return DeleteSwapInfo((byte[])args[0]);
                    case "transferOwner":
                        if (args.Length != 1)
                            return "code:0, msg:The transferOwner method need one parameter.";
                        return TransferOwner((byte[])args[0]);
                }
                
            }
            return false;
        }

        // user lock
        [DisplayName("userLock")]
        private static object UserLock(byte[] keyHash, byte[] from, byte[] to, BigInteger amount, byte[] wrapperInAddress, BigInteger overtimeBlocks) {
            // params length check
            if (keyHash.Length<=0 || from.Length!=20 || wrapperInAddress.Length!=20) {
                return "code:0, msg:The parameters error";
            }
            
            // Whether keyHash already exists
            StorageMap SWAP_MAP = Storage.CurrentContext.CreateMap(nameof(SWAP_MAP));
            var result = SWAP_MAP.Get(keyHash);
            if (result.Length != 0) {
                return "code:0, msg:The keyHash already exists.";
            }

            // amount check
            if (amount < MinSwapAmount) {
                return "code:0, msg:The Minimum swap amout is too little";
            }

            // check overtime blocks
            if (overtimeBlocks < NEP5ToERC20OvertimeBlocks) {
                overtimeBlocks = NEP5ToERC20OvertimeBlocks;
            }

            // transfer qlc from user to contract
            object[] transferParams = new object[3];
            transferParams[0] = from;
            transferParams[1] = to;
            transferParams[2] = amount;
            if (QlcMain("transfer", transferParams)) {
                // swap info
                var swapInfo = new SwapInfo()
                {
                    userNeoAddress = from,
                    wrapperNeoAddress = wrapperInAddress,
                    amount = amount,
                    lockTimestamp = Blockchain.GetBlock(Blockchain.GetHeight()).Timestamp,
                    blockHeight = Blockchain.GetHeight(),
                    overtimeBlocks = overtimeBlocks,
                    type = 1,
                    state = 1
                };

                var txid = (ExecutionEngine.ScriptContainer as Transaction).Hash;
                swapInfo.txidIn = txid;
                SWAP_MAP.Put(keyHash, Helper.Serialize(swapInfo));
                
                // event
                UserLockEvent(keyHash, 0);
                return txid;
            } else {
                return "code:0, msg:transfer error.";
            }

        }

        // wrapper unlock
        [DisplayName("wrapperUnlock")]
        private static object WrapperUnlock(string key, byte[] from, byte[] userEthAddress) {
            // params length check
            if (key.Length <= 0) {
                return "code:0, msg:The parameters error.";
            }

            // get swap info
            var keyHash = SmartContract.Sha256(key.AsByteArray());
            StorageMap SWAP_MAP = Storage.CurrentContext.CreateMap(nameof(SWAP_MAP));
            var result = SWAP_MAP.Get(keyHash);
            if (result.Length == 0) {
                return "code:0, msg:The swap info not found.";
            }
            SwapInfo swapInfo = Helper.Deserialize(result) as SwapInfo;
            
            // timeout check
            if (Blockchain.GetHeight() - swapInfo.blockHeight > swapInfo.overtimeBlocks) {
                return "code:0, msg:The swap has timed out.";
            }

            // state check
            if (1 != swapInfo.state) {
                return "code:0, msg:The state doesn't meet the criteria.";
            }

            // transfer qlc from contract to wrapper
            object[] transferParams = new object[3];
            transferParams[0] = from;
            transferParams[1] = swapInfo.wrapperNeoAddress;
            transferParams[2] = swapInfo.amount;
            if (QlcMain("transfer", transferParams)) {
                swapInfo.originText = key;
                var txid = (ExecutionEngine.ScriptContainer as Transaction).Hash;
                swapInfo.userEthAddress = userEthAddress;
                swapInfo.txidOut = txid;
                swapInfo.unLockTimestamp = Blockchain.GetBlock(Blockchain.GetHeight()).Timestamp;
                swapInfo.state = 2;
                SWAP_MAP.Put(keyHash, Helper.Serialize(swapInfo));
                
                // event
                WrapperUnlockEvent(keyHash, 1);
                return txid;
            } else {
                return "code:0, msg:transfer qlc to wrapper fail.";
            }
            
        }

        // refund user
        [DisplayName("refundUser")]
        private static object RefundUser(string key, byte[] from) {
            // params length
            if (key.Length <= 0) {
                return "code:0, msg:The parameters error.";
            }

            // get swap info
            var keyHash = SmartContract.Sha256(key.AsByteArray());
            StorageMap SWAP_MAP = Storage.CurrentContext.CreateMap(nameof(SWAP_MAP));
            var result = SWAP_MAP.Get(keyHash);
            if (result.Length == 0) {
                return "code:0, msg:The swap info not found.";
            }
            SwapInfo swapInfo = Helper.Deserialize(result) as SwapInfo;

            // timeout check
            if (Blockchain.GetHeight() - swapInfo.blockHeight <= swapInfo.overtimeBlocks) {
                return "code:0, msg:No timeout.";
            }

            // state check
            if (1 != swapInfo.state) {
                return "code:0, msg:The state doesn't meet the criteria.";
            }

            // refund qlc from contract to user
            object[] transferParams = new object[3];
            transferParams[0] = from;
            transferParams[1] = swapInfo.userNeoAddress;
            transferParams[2] = swapInfo.amount;
            if (QlcMain("transfer", transferParams)) {
                swapInfo.originText = key;
                var txid = (ExecutionEngine.ScriptContainer as Transaction).Hash;
                swapInfo.txidRefund = txid;
                swapInfo.refundTimestamp = Blockchain.GetBlock(Blockchain.GetHeight()).Timestamp;
                swapInfo.state = 3;
                SWAP_MAP.Put(keyHash, Helper.Serialize(swapInfo));
                
                // event
                RefundUserEvent(keyHash, 2);
                return txid;
            } else {
                return "code:0, msg:Refund qlc to user fail.";
            }

        }
        
        // wrapper lock
        [DisplayName("wrapperLock")]
        private static object WrapperLock(byte[] keyHash, byte[] from, byte[] to, BigInteger amount, byte[] userEthAddress, BigInteger overtimeBlocks) {
            // params length check
            if (keyHash.Length<=0 || from.Length!=20) {
                return "code:0, msg:The parameters error";
            }
            
            // Whether keyHash already exists
            StorageMap SWAP_MAP = Storage.CurrentContext.CreateMap(nameof(SWAP_MAP));
            var result = SWAP_MAP.Get(keyHash);
            if (result.Length != 0) {
                return "code:0, msg:The keyHash already exists.";
            }

            // check overtime blocks
            if (overtimeBlocks < ERC20ToNEP5OvertimeBlocks) {
                overtimeBlocks = ERC20ToNEP5OvertimeBlocks;
            }

            // transfer qlc from wrapper to contract
            object[] transferParams = new object[3];
            transferParams[0] = from;
            transferParams[1] = to;
            transferParams[2] = amount;
            if (QlcMain("transfer", transferParams)) {
                // swap info
                var swapInfo = new SwapInfo() {
                    wrapperNeoAddress = from,
                    userEthAddress = userEthAddress,
                    amount = amount,
                    lockTimestamp = Blockchain.GetBlock(Blockchain.GetHeight()).Timestamp,
                    blockHeight = Blockchain.GetHeight(),
                    overtimeBlocks = overtimeBlocks,
                    type = 2,
                    state = 4
                };

                var txid = (ExecutionEngine.ScriptContainer as Transaction).Hash;
                swapInfo.txidIn = txid;
                SWAP_MAP.Put(keyHash, Helper.Serialize(swapInfo));
                
                // event
                WrapperLockEvent(keyHash, 3);
                return txid;
            } else {
                return "code:0, msg:transfer error.";
            }

        }
        
        // user unlock
        [DisplayName("userUnlock")]
        private static object UserUnlock(string key, byte[] from, byte[] to) {
            // params length check
            if (key.Length<=0 || to.Length!=20) {
                return "code:0, msg:The parameters error.";
            }

            // get swap info
            var keyHash = SmartContract.Sha256(key.AsByteArray());
            StorageMap SWAP_MAP = Storage.CurrentContext.CreateMap(nameof(SWAP_MAP));
            var result = SWAP_MAP.Get(keyHash);
            if (result.Length == 0) {
                return "code:0, msg:The swap info not found.";
            }
            SwapInfo swapInfo = Helper.Deserialize(result) as SwapInfo;

            // timeout check
            if (Blockchain.GetHeight() - swapInfo.blockHeight > swapInfo.overtimeBlocks) {
                return "code:0, msg:The swap has timed out.";
            }

            // state check
            if (4 != swapInfo.state) {
                return "code:0, msg:The state doesn't meet the criteria.";
            }

            // transfer qlc from contract to user
            object[] transferParams = new object[3];
            transferParams[0] = from;
            transferParams[1] = to;
            transferParams[2] = swapInfo.amount;
            if (QlcMain("transfer", transferParams)) {
                var txid = (ExecutionEngine.ScriptContainer as Transaction).Hash;
                swapInfo.originText = key;
                swapInfo.userNeoAddress = to;
                swapInfo.txidOut = txid;
                swapInfo.unLockTimestamp = Blockchain.GetBlock(Blockchain.GetHeight()).Timestamp;
                swapInfo.state = 5;
                SWAP_MAP.Put(keyHash, Helper.Serialize(swapInfo));
                
                // event
                UserUnlockEvent(keyHash, 4);
                return txid;
            } else {
                return "code:0, msg:transfer qlc to wrapper fail.";
            }
        }

        // refund wrapper
        [DisplayName("refundWrapper")]
        private static object RefundWrapper(byte[] keyHash, byte[] from) {
            // params length check
            if (keyHash.Length <= 0) {
                return "code:0, msg:The parameters error.";
            }

            // get swap info
            StorageMap SWAP_MAP = Storage.CurrentContext.CreateMap(nameof(SWAP_MAP));
            var result = SWAP_MAP.Get(keyHash);
            if (result.Length == 0) {
                return "code:0, msg:The swap info not found.";
            }
            SwapInfo swapInfo = Helper.Deserialize(result) as SwapInfo;

            // timeout check
            if (Blockchain.GetHeight() - swapInfo.blockHeight <= swapInfo.overtimeBlocks) {
                return "code:0, msg:No timeout.";
            }

            // state check
            if (4 != swapInfo.state) {
                return "code:0, msg:The state doesn't meet the criteria.";
            }

            // refund qlc from contract to wrapper
            object[] transferParams = new object[3];
            transferParams[0] = from;
            transferParams[1] = swapInfo.wrapperNeoAddress;
            transferParams[2] = swapInfo.amount;
            if (QlcMain("transfer", transferParams)) {
                var txid = (ExecutionEngine.ScriptContainer as Transaction).Hash;
                swapInfo.txidRefund = txid;
                swapInfo.refundTimestamp = Blockchain.GetBlock(Blockchain.GetHeight()).Timestamp;
                swapInfo.state = 6;
                SWAP_MAP.Put(keyHash, Helper.Serialize(swapInfo));
                
                // event
                RefundWrapperEvent(keyHash, 5);
                return txid;
            } else {
                return "code:0, msg:Refund qlc to wrapper fail.";
            }

        }
        
        // initialization parameters, only once
        [DisplayName("init")]
        private static bool Init() {
            byte[] owner = Storage.Get(Storage.CurrentContext, "Owner");
            if (owner.Length != 0) return false;
            Storage.Put(Storage.CurrentContext, "Owner", InitOwner);
            return true;
        }

        // transfer Owner
        [DisplayName("transferOwner")]
        private static object TransferOwner(byte[] newOwner) {
            // new owner address
            if (newOwner.Length != 20) {
                return "code:0, msg:The new Owner error.";
            }
            Storage.Put(Storage.CurrentContext, "Owner", newOwner);
            return "code:1, msg:transfer success.";

        }

        // delete swap info
        [DisplayName("deleteSwapInfo")]
        private static object DeleteSwapInfo(byte[] keyHash) {
            // get swap info
            StorageMap SWAP_MAP = Storage.CurrentContext.CreateMap(nameof(SWAP_MAP));
            var result = SWAP_MAP.Get(keyHash);
            if (result.Length == 0) {
                return "code:0, msg:The swap info not found.";
            }
            SwapInfo swapInfo = Helper.Deserialize(result) as SwapInfo;

            // state check
            if (swapInfo.state==1 || swapInfo.state==4) {
                return "code:0, msg:Not completed swap info.";
            }
            SWAP_MAP.Delete(keyHash);
            return true;

        }

        // return storage owner
        private static byte[] GetOwner() {
            return Storage.Get(Storage.CurrentContext, "Owner");
        }
        
        //主网合约地址： 0d821bd7b6d53f5c2b40e217c6defc8bbe896cf5
        //测试网合约地址： b9d7ea3062e6aeeb3e8ad9548220c4ba1361d263
        [Appcall("b9d7ea3062e6aeeb3e8ad9548220c4ba1361d263")]
        private static extern bool QlcMain(string operation, params object[] args);
    }
}
