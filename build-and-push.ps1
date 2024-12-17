# Le a versão atual do arquivo version.txt
$IMAGE_VERSION = Get-Content -Path "version.txt"

# Incrementa a versão
$versionParts = $IMAGE_VERSION -split '\.'

# Incrementa a última parte da versão
$versionParts[-1] = [int]$versionParts[-1] + 1

# Reconstrução da versão
$NEW_VERSION = ($versionParts -join '.')

# Atualiza o arquivo version.txt com a nova versão
Set-Content -Path "version.txt" -Value $NEW_VERSION

Write-Output "Nova versão gerada: $NEW_VERSION"

# Define a tag da imagem
$IMAGE_TAG = "leovilela100/aws-project-siecola:$NEW_VERSION"

Write-Output "Construindo a imagem Docker com a tag: $IMAGE_TAG"

# Reconstrução da imagem usando o docker-compose com a nova versão
$env:IMAGE_VERSION = $NEW_VERSION
docker-compose build
if ($LASTEXITCODE -ne 0) {
    Write-Output "Erro ao construir a imagem Docker."
    exit 1
}

# Loga no Docker Hub
docker login
if ($LASTEXITCODE -ne 0) {
    Write-Output "Erro ao fazer login no Docker Hub."
    exit 1
}

# Faz o push da nova versão para o Docker Hub
Write-Output "Enviando a imagem $IMAGE_TAG para o Docker Hub..."
docker push $IMAGE_TAG
if ($LASTEXITCODE -ne 0) {
    Write-Output "Erro ao enviar a imagem para o Docker Hub."
    exit 1
}

Write-Output "Imagem $IMAGE_TAG enviada para o Docker Hub com sucesso!"
