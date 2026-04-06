# ⭐ StellarCore

**ᴇᴄᴏɴᴏᴍʏ ᴘʟᴜɢɪɴ ꜰᴏʀ ᴍɪɴᴇᴄʀᴀꜰᴛ**

[![Version](https://img.shields.io/badge/version-1.0.0-blue)]()
[![Java](https://img.shields.io/badge/java-17-red)]()
[![Minecraft](https://img.shields.io/badge/minecraft-1.20-green)]()
[![Build](https://github.com/OnlyXenzz/StellarCore/actions/workflows/build.yml/badge.svg)]()

---

## 📖 ᴅᴇꜱᴄʀɪᴘᴛɪᴏɴ

**StellarCore** adalah plugin economy lengkap untuk Minecraft dengan sistem balance, transfer, daily reward, interest, dan shop.

---

## ✨ ꜰᴇᴀᴛᴜʀᴇꜱ

| Fitur | Deskripsi |
|-------|-----------|
| 💰 Balance | Cek saldo kapan saja |
| 📤 Transfer | Kirim uang ke player lain |
| 👑 Top Leaderboard | Lihat 10 player terkaya |
| 📅 Daily Reward | Klaim reward setiap hari (bonus streak) |
| 📈 Interest System | Dapat bunga setiap jam |
| 🛒 Shop | Beli item dengan kategori (100+ item) |
| 🛡️ Admin Commands | Give, set, take balance |

---

## 📝 ᴄᴏᴍᴍᴀɴᴅꜱ

### Player Commands
| Command | Description |
|---------|-------------|
| `/sc menu` | Menu utama |
| `/sc balance` | Cek balance kamu |
| `/sc pay <player> <amount>` | Kirim uang ke player lain |
| `/sc top` | Lihat 10 player terkaya |
| `/sc daily` | Klaim reward harian |
| `/sc shop` | Buka shop |
| `/sc status` | Lihat status kamu |

### Admin Commands
| Command | Description |
|---------|-------------|
| `/sc reload` | Reload config |
| `/sc give <player> <amount>` | Beri uang ke player |
| `/sc set <player> <amount>` | Set balance player |
| `/sc take <player> <amount>` | Ambil uang dari player |
| `/sc stats` | Lihat statistik plugin |
| `/sc debug` | Toggle debug mode |

---

## 🎮 ᴘᴇʀᴍɪꜱꜱɪᴏɴ

| Permission | Description |
|------------|-------------|
| `stellar.admin` | Akses semua admin commands |

---

## 📥 ɪɴꜱᴛᴀʟʟᴀᴛɪᴏɴ

1. **Download** `StellarCore.jar` dari [Releases](https://github.com/StellarProject-Dev/StellarCore/releases)
2. **Copy** ke folder `plugins/` server Minecraft
3. **Restart** server
4. **Konfigurasi** di `plugins/StellarCore/config.yml`

---

## ⚙️ ᴄᴏɴꜰɪɢᴜʀᴀᴛɪᴏɴ

File `config.yml`:

```yaml
economy:
  starting_balance: 100
  currency_symbol: "⍟"
  min_pay: 1

daily_reward:
  enabled: true
  base_amount: 100
  bonus_per_day: 10
  max_bonus_days: 30
  reset_after_days: 2

interest:
  enabled: true
  rate: 1.5
  interval_minutes: 60
  max_interest: 1000
  min_balance_for_interest: 100
