using UnityEngine;

public class Kamikaze : MonoBehaviour
{
    public int damageAmount = 10;          // Daño por ataque
    public float attackCooldown = 1.5f;    // Tiempo entre ataques
    private float _nextAttackTime;          // Cuándo podrá atacar de nuevo
    private bool _hasAttacked = false;      // Para evitar múltiples ataques

    private void OnTriggerEnter(Collider other)
    {
        if (other.CompareTag("Base"))
        {
            TryAttackBase(other);
            
            // Destruye el kamikaze tras atacar (comportamiento único)
            if (!_hasAttacked)
            {
                _hasAttacked = true;
                Destroy(gameObject);
            }
        }
    }

    private void OnTriggerStay(Collider other)
    {
        TryAttackBase(other);
    }

    private void TryAttackBase(Collider baseCollider)
    {
        if (baseCollider.CompareTag("Base") && Time.time >= _nextAttackTime)
        {
            Base baseScript = baseCollider.GetComponent<Base>();
            if (baseScript != null)
            {
                baseScript.Damage(damageAmount);
                _nextAttackTime = Time.time + attackCooldown;

                Debug.Log($"[KAMIKAZE] Atacó la Base | " +
                          $"Daño: {damageAmount} | " +
                          $"Vida restante: {baseScript.HP}");
            }
        }
    }
}