// See LICENSE for license details.

package crypto.aes

import chisel3._

/**
 * Key Expansion
 */
class KeyExpansion extends Module {
  val io = IO(new Bundle {
    val key_in = Input(UInt(256.W))
    val key_out = Output(UInt(256.W))
  })

  val rcon = VecInit(Seq(0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36).map(_.U))

  io.key_out := DontCare
}
