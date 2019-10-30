// See LICENSE for license details.

package crypto.aes

import chisel3._
import chisel3.experimental.ChiselEnum
import chisel3.util._

/**
 * AES mode
 */
object AESMode extends ChiselEnum {
  val enc, dec = Value
}

/**
 * AES configuration
 */
class AESCfgs extends Bundle {
  val key = UInt(256.W)
  val nk = UInt(4.W)
  val mode = UInt(AESMode.getWidth.W)
}

/**
 * AES I/O
 */
class AESTopIO(decoupledCfg: Boolean = true) extends Bundle {
  //val cfg = if (decoupledCfg) DecoupledIO(new AESCfgs) else new AESCfgs
  val cfg = Input(new AESCfgs)
  val data_in = Flipped(DecoupledIO(UInt(256.W)))
  val data_out = DecoupledIO(UInt(256.W))

  override def cloneType: this.type = new AESTopIO(decoupledCfg).asInstanceOf[this.type]
}

/**
 * AES top module
 */
class AESTop extends Module {
  val io = IO(new AESTopIO)

  io := DontCare
  io.data_in.ready := true.B
}
