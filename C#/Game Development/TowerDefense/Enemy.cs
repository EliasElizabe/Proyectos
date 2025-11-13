using UnityEngine;

public class Enemy : MonoBehaviour
{
    public int damageAmount = 10;          // Daño por ataque
    public float attackCooldown = 1.5f;    // Tiempo entre ataques

    private float _nextAttackTime;          // Cuándo podrá atacar de nuevo

    private void OnTriggerEnter(Collider other)
    {
        TryAttackBase(other);
    }

    private void OnTriggerStay(Collider other)
    {
        TryAttackBase(other);
    }

    private void TryAttackBase(Collider baseCollider)
    {
        // 1. Verifica si es la Base y si ya pasó el tiempo de enfriamiento
        if (baseCollider.CompareTag("Base") && Time.time >= _nextAttackTime)
        {
            Base baseScript = baseCollider.GetComponent<Base>();
            if (baseScript != null)
            {
                // 2. Aplica daño y actualiza el próximo tiempo de ataque
                baseScript.Damage(damageAmount);
                _nextAttackTime = Time.time + attackCooldown; // Cooldown

                // 3. Muestra en consola el resultado
                Debug.Log($"[ENEMY] Atacó la Base | " +
                          $"Daño: {damageAmount} | " +
                          $"Vida restante: {baseScript.HP}");
            }
        }
    }
    public int HP = 30;  // Vida del enemigo (ajusta el valor inicial según necesites)

    // Método que la Torre llamará para aplicar daño
    public void TakeDamage(int damageAmount)
{
    HP -= damageAmount;
    Debug.Log($"{name} recibió {damageAmount} de daño (HP: {HP})"); // Verifica este log
    if (HP <= 0) Destroy(gameObject);
}

    private void Die()
    {
        Debug.Log($"{gameObject.name} fue eliminado!");
        Destroy(gameObject);  // Destruye el enemigo al morir
    }
}
