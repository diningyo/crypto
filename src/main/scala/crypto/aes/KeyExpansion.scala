// See LICENSE for license details.

package crypto.aes

import chisel3._
import chisel3.util._

/**
 * Key Expansion
 */
class KeyExpansion extends Module {
  val io = IO(new Bundle {
    val nk = Input(UInt(4.W))
    val key_in = Input(Vec(32, UInt(8.W)))
    val w_in = Input(Vec(60, UInt(32.W)))
    val w_out = Output(Vec(60, UInt(32.W)))
  })

  val rcon = VecInit(Seq(0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36).map(_.U))

  val w_key_expansion_table = VecInit(Seq(4, 6, 8).map {
    i =>
      val w = Wire(Vec(60, UInt(32.W)))
      w := DontCare
      Range(0, i * 4, 4).foreach(i => w(i) := Cat(io.key_in.slice(i, i + 4)))
      w
  })

  io.w_out := w_key_expansion_table(io.nk)
}

object ElaborateKeyExpansion extends App {
  Driver.execute(Array(
    "-td=rtl"
  ),
    () => new KeyExpansion
  )

  Driver.execute(Array(
    "-td=rtl"
  ),
    () => new Module {
      val io = IO(new Bundle {
        val out_clock = Output(Clock())
        val out_reset = Output(Bool())
      })

      val d_reset = RegNext(reset).asAsyncReset()
      val r_reset = withReset(d_reset) { RegInit(false.B) }

      r_reset := 1.U

      io.out_clock := clock
      io.out_reset := r_reset
    }
  )
}