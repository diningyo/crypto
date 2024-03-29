// See LICENSE for license details.

package test.util

import chisel3._
import chisel3.util._

/**
 * Watchdog Timer
 * @param limit Maximum cycles of simulation.
 * @param abortEn True if timeout is occurred, simulation would finish by assert.
 */
class WDT(limit: Int, abortEn: Boolean) extends Module {
  val io = IO(new Bundle {
    val timeout = Output(Bool())
  })

  val timer = RegInit(0.U(log2Ceil(limit).W))

  when (timer =/= limit.U) {
    timer := timer + 1.U
  }

  val timeout = timer === limit.U

  if (abortEn) {
    assert(!timeout, "WDT has expired!!")
  } else {
    printf("WDT has expired!!")
  }

  io.timeout := timeout
}