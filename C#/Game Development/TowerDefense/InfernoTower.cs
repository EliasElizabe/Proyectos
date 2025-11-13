using UnityEngine;

public class InfernoTower : MonoBehaviour
{
    [Header("Configuración Inferno")]
    public float attackRange = 4f;
    public int minDamage = 5;
    public int maxDamage = 50;
    public float damageRampTime = 10f; // Tiempo para alcanzar maxDamage
    public float attackCooldown = 0.5f;

    [Header("Referencias")]
    public LayerMask enemyLayer;
    public ParticleSystem fireEffect;
    public Gradient damageGradient; // Color del efecto según daño (opcional)

    private Transform _currentTarget;
    private float _nextAttackTime;
    private float _damageProgress; // Progreso de 0 a 1
    private float _timeOnTarget;

    private void Update()
    {
        if (_currentTarget == null || !IsTargetInRange())
        {
            FindNewTarget();
            ResetDamageProgress();
        }

        if (_currentTarget != null)
        {
            UpdateDamageProgress();
            
            if (Time.time >= _nextAttackTime)
            {
                Attack();
            }
        }
    }

    private bool IsTargetInRange()
    {
        return Vector3.Distance(transform.position, _currentTarget.position) <= attackRange;
    }

    private void FindNewTarget()
    {
        Collider[] enemiesInRange = Physics.OverlapSphere(transform.position, attackRange, enemyLayer);
        _currentTarget = enemiesInRange.Length > 0 ? enemiesInRange[0].transform : null;
    }

    private void UpdateDamageProgress()
    {
        _timeOnTarget += Time.deltaTime;
        _damageProgress = Mathf.Clamp01(_timeOnTarget / damageRampTime); // Normalizado a [0, 1]
    }

    private void ResetDamageProgress()
    {
        _timeOnTarget = 0f;
        _damageProgress = 0f;
        if (fireEffect != null) fireEffect.Stop();
    }

    private void Attack()
    {
        if (_currentTarget.TryGetComponent<Enemy>(out Enemy enemy))
        {
            int currentDamage = Mathf.RoundToInt(Mathf.Lerp(minDamage, maxDamage, _damageProgress));
            enemy.TakeDamage(currentDamage);
            _nextAttackTime = Time.time + attackCooldown;

            // Debug visual en la escena
            Debug.Log($"🔥 [Inferno] Daño: {currentDamage} | Progreso: {_damageProgress * 100:F0}%");
            
            UpdateVisualEffects();
        }
        else
        {
            _currentTarget = null;
        }
    }

    private void UpdateVisualEffects()
    {
        if (fireEffect != null)
        {
            if (!fireEffect.isPlaying) fireEffect.Play();
            
            // Cambia color según progreso (opcional)
            var mainModule = fireEffect.main;
            mainModule.startColor = damageGradient.Evaluate(_damageProgress);
            
            fireEffect.transform.LookAt(_currentTarget);
        }
    }

    private void OnDrawGizmosSelected()
    {
        Gizmos.color = Color.red;
        Gizmos.DrawWireSphere(transform.position, attackRange);
        
        // Debug: Muestra el progreso actual en el Editor
        if (_currentTarget != null)
        {
            GUI.color = Color.white;
            UnityEditor.Handles.Label(
                transform.position + Vector3.up * 2f, 
                $"Daño: {Mathf.Lerp(minDamage, maxDamage, _damageProgress):F1}\nProgreso: {_damageProgress * 100:F0}%"
            );
        }
    }
}
