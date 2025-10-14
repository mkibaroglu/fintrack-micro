# FinTrack Micro

FinTrack Micro, kişisel finans harcamalarını ve birikimlerini takip etmeye yarayan, 
mikroservis tabanlı bir finans yönetim sistemidir.  
Amaç, kullanıcıların gelir-gider hareketlerini yönetmesini, hedeflerini takip etmesini 
ve finansal farkındalığını artırmasını sağlamaktır.

---

## 🚀 Tech Stack
- **Backend:** Java 17, Spring Framework, Redis, Docker
- **Frontend:** React
- **Infrastructure:** Docker Compose, GitHub Actions (CI/CD)
- **Architecture:** Microservices (Auth, Transaction, Gateway, Config Server)

---

## 🧩 Proje Yapısı
fintrack-micro/
├─ backend/
│ ├─ auth-service/
│ ├─ transaction-service/
│ ├─ gateway/
│ └─ config-server/
├─ frontend/
├─ infra/
│ └─ docker-compose.yml
├─ docs/
└─ .github/
└─ workflows/

---

## 🏃‍♂️ Hızlı Başlangıç
> Not: Bu adımlar placeholder’dır. Altyapı henüz tamamlanmadı.

```bash
# 1. Depoyu klonla
git clone https://github.com/mkibaroglu/fintrack-micro.git
cd fintrack-micro

# 2. Docker altyapısını ayağa kaldır
docker-compose up

Kaydet → ✅  
Bu dosya hazır.




