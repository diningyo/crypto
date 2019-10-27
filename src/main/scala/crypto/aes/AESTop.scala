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
 * AES I/O
 */
class AESTopIO extends Bundle {
  val key = Input(UInt(256.W))
  val nk = Input(UInt(log2Ceil(8).W))
  val mode = Input(UInt(AESMode.getWidth.W))
  val data_in = Flipped(DecoupledIO(UInt(256.W)))
  val data_out = DecoupledIO(UInt(256.W))
}

/**
 * AES top module
 */
class AESTop extends Module {
  val io = IO(new AESTopIO)

  io := DontCare
}
