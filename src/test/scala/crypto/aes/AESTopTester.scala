// See LICENSE for license details.

package crypto.aes

import javax.crypto.Cipher
import chisel3._
import chisel3.iotesters._
import javax.crypto.spec.SecretKeySpec

/**
 * Unit test class for AESTop
 * @param c Instance of AESTop.
 */
class AESTopUnitTester(c: SimDTMAESTop) extends PeekPokeTester(c) {
  val aes = c.io.aes

  def setCfg(mode: AESMode.Type, key: String): Unit = {
    val keyBits = hex2bytes(key).length * 8
    val nk = BigInt(keyBits / 32)
    val keyData = keyBits match {
      case 128 => BigInt(key, 16) << 128
      case 192 => BigInt(key, 16) << 64
      case _ => BigInt(key, 16)
    }
    println(s"$nk")
    poke(aes.cfg.mode, mode)
    poke(aes.cfg.key, keyData)
    poke(aes.cfg.nk, nk)

  }

  def hex2bytes(hex: String): Array[Byte] =
    hex.replaceAll("[^0-9A-Fa-f]", "").sliding(2, 2).toArray.map(Integer.parseInt(_, 16).toByte)
}

/**
 * Test class for AESTop
 */
class AESTopTester extends ChiselFlatSpec {

  val dut = "AESTop"
  val testDir = s"test_run_dir/$dut"
  val limit = 1000

  behavior of dut

  val defaultArgs = Array(
    s"-tn=$dut",
    "-tbn=treadle",
    "-tgvo=on"
  )

  it should "be able to encrypt FIP-197 C.1 AES-128 example [AES-0001]" in {
    iotesters.Driver.execute(
      defaultArgs :+ s"-td=$testDir/0001",
    () => new SimDTMAESTop()(limit)) {
      c => new AESTopUnitTester(c) {
        val mode = AESMode.enc
        val plainText = "00112233445566778899aabbccddeeff"
        val key = "000102030405060708090a0b0c0d0e0f"
        val exp = ""

        setCfg(mode, key)
        step(1)
      }
    } should be (true)
  }

  it should "be able to encrypt FIP-197 C.2 AES-192 example [AES-0002]" in {
    iotesters.Driver.execute(
      defaultArgs :+ s"-td=$testDir/0002",
      () => new SimDTMAESTop()(limit)) {
      c => new AESTopUnitTester(c) {
        val mode = AESMode.enc
        val plainText = "00112233445566778899aabbccddeeff"
        val key = "000102030405060708090a0b0c0d0e0f1011121314151617"

        setCfg(mode, key)
        step(1)
      }
    } should be (true)
  }

  it should "be able to encrypt FIP-197 C.3 AES-256 example [AES-0003]" in {
    iotesters.Driver.execute(
      defaultArgs :+ s"-td=$testDir/0003",
      () => new SimDTMAESTop()(limit)) {
      c => new AESTopUnitTester(c) {
        val mode = AESMode.enc
        val plainText = "00112233445566778899aabbccddeeff"
        val key = "000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f"

        setCfg(mode, key)
        step(1)
      }
    } should be (true)
  }
}
