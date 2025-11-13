using UnityEngine;

public class AttackTower : MonoBehaviour
{
    [Header("Configuración de Ataque")]
    public float attackRange = 5f;          // Rango de ataque
    public int damage = 15;                 // Daño por ataque
    public float attackCooldown = 2f;       // Tiempo entre ataques
    
    [Header("Referencias")]
    public LayerMask enemyLayer;            // Capa de enemigos (configúrala en el Inspector)
    public Transform attackPoint;           // Punto de origen del ataque (opcional para efectos)
    
    private Transform _currentTarget;       // Enemigo actualmente seleccionado
    private float _nextAttackTime;          // Tiempo para el próximo ataque

    private void Update()
    {
        // 1. Busca enemigos en rango cada frame
        if (_currentTarget == null || Vector3.Distance(transform.position, _currentTarget.position) > attackRange)
        {
            FindClosestEnemy();
        }

        // 2. Si hay objetivo y está en rango, ataca
        if (_currentTarget != null && Time.time >= _nextAttackTime)
        {
            Attack();
        }
    }

    // Busca el enemigo más cercano dentro del rango
    private void FindClosestEnemy()
    {
        Collider[] enemiesInRange = Physics.OverlapSphere(transform.position, attackRange, enemyLayer);
        float closestDistance = Mathf.Infinity;
        _currentTarget = null;

        foreach (Collider enemy in enemiesInRange)
        {
            float distance = Vector3.Distance(transform.position, enemy.transform.position);
            if (distance < closestDistance)
            {
                closestDistance = distance;
                _currentTarget = enemy.transform;
            }
        }
    }

    // Lógica de ataque
    private void Attack()
    {
        if (_currentTarget.TryGetComponent<Enemy>(out Enemy enemyScript))
        {
            enemyScript.TakeDamage(damage); // Asegúrate de que tu script Enemy tenga este método
            _nextAttackTime = Time.time + attackCooldown;
            
            Debug.Log($"🏰 Torre atacó a {_currentTarget.name} | Daño: {damage}");
            
            // Opcional: Efectos visuales/sonido
            // Instantiate(projectilePrefab, attackPoint.position, Quaternion.identity);
        }
        else
        {
            _currentTarget = null; // Si el objetivo ya no es un enemigo, deselecciona
        }
    }

    // Dibuja el rango en el Editor (solo para debug)
    private void OnDrawGizmosSelected()
    {
        Gizmos.color = Color.red;
        Gizmos.DrawWireSphere(transform.position, attackRange);
    }
}