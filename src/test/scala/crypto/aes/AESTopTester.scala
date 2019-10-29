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

  def hex2bytes(hex: String): Array[Byte] =
    hex.replaceAll("[^0-9A-Fa-f]", "").sliding(2, 2).toArray.map(Integer.parseInt(_, 16).toByte)

  def padding(data: String, numOfRequiredBits: Int): BigInt = {
    0
  }

  /**
   * Set AES configuration
   * @param mode AES mode (enc or dec).
   * @param key AES key.
   */
  def setCfg(mode: AESMode.Type, key: String): Unit = {
    val keyBits = hex2bytes(key).length * 8
    val nk = BigInt(keyBits / 32)
    val keyData = keyBits match {
      case 128 => BigInt(key, 16) << 128
      case 192 => BigInt(key, 16) << 64
      case _ => BigInt(key, 16)
    }

    poke(aes.cfg.mode, mode)
    poke(aes.cfg.key, keyData)
    poke(aes.cfg.nk, nk)
  }

  /**
   * Run AES calculation
   * @param data Data
   */
  def run(data: String): Unit = {
    val dataBits = BigInt(data, 16)
    poke(aes.data_in.valid, true)
    poke(aes.data_in.bits, dataBits)

    if (peek(aes.data_in.ready) != 1) {
      while (peek(aes.data_in.ready) != 1) {
        step(1)
      }
    } else {
      step(1)
    }
  }
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

  it should "be able to encrypt and decrypt FIP-197 C.1 AES-128 example [AES-0001]" in {
    iotesters.Driver.execute(
      defaultArgs :+ s"-td=$testDir/0001",
    () => new SimDTMAESTop()(limit)) {
      c => new AESTopUnitTester(c) {
        val key = "000102030405060708090a0b0c0d0e0f"
        val plainText = "00112233445566778899aabbccddeeff"
        val cipherText = "69c4e0d86a7b0430d8cdb78070b4c55a"

        // encrypt
        setCfg(AESMode.enc, key)
        run(plainText)
        step(1)

        // decrypt
        setCfg(AESMode.dec, key)
        run(cipherText)
        step(1)
      }
    } should be (true)
  }

  it should "be able to encrypt and decrypt FIP-197 C.2 AES-192 example [AES-0002]" in {
    iotesters.Driver.execute(
      defaultArgs :+ s"-td=$testDir/0002",
      () => new SimDTMAESTop()(limit)) {
      c => new AESTopUnitTester(c) {
        val key = "000102030405060708090a0b0c0d0e0f1011121314151617"
        val plainText = "00112233445566778899aabbccddeeff"
        val cipherText = "dda97ca4864cdfe06eaf70a0ec0d7191"

        // encrypt
        setCfg(AESMode.enc, key)
        run(plainText)
        step(1)

        // decrypt
        setCfg(AESMode.dec, key)
        run(cipherText)
        step(1)
      }
    } should be (true)
  }

  it should "be able to encrypt and decrypt FIP-197 C.3 AES-256 example [AES-0003]" in {
    iotesters.Driver.execute(
      defaultArgs :+ s"-td=$testDir/0003",
      () => new SimDTMAESTop()(limit)) {
      c => new AESTopUnitTester(c) {
        val key = "000102030405060708090a0b0c0d0e0f101112131415161718191a1b1c1d1e1f"
        val plainText = "00112233445566778899aabbccddeeff"
        val cipherText = "8ea2b7ca516745bfeafc49904b496089"

        // encrypt
        setCfg(AESMode.enc, key)
        run(plainText)
        step(1)

        // decrypt
        setCfg(AESMode.dec, key)
        run(cipherText)
        step(1)
      }
    } should be (true)
  }
}
